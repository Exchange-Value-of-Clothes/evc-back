package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.member.dto.MemberRequest.EmailSignup;
import com.yzgeneration.evc.member.enums.MemberRole;
import com.yzgeneration.evc.member.enums.MemberStatus;
import com.yzgeneration.evc.member.enums.ProviderType;
import com.yzgeneration.evc.member.model.Member;
import com.yzgeneration.evc.mock.StubUuidHolder;
import com.yzgeneration.evc.mock.member.DummyEmailSender;
import com.yzgeneration.evc.mock.member.FakeEmailVerificationRepository;
import com.yzgeneration.evc.mock.member.FakeMemberRepository;
import com.yzgeneration.evc.mock.member.SpyPasswordProcessor;
import com.yzgeneration.evc.mock.StubRandomHolder;
import com.yzgeneration.evc.verification.enums.EmailVerificationType;
import com.yzgeneration.evc.verification.implement.EmailVerificationProcessor;
import com.yzgeneration.evc.verification.model.EmailVerification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.yzgeneration.evc.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberAuthenticationServiceTest {

    private MemberAuthenticationService memberAuthenticationService;

    @BeforeEach
    void init() {
        EmailVerificationProcessor emailVerificationProcessor = EmailVerificationProcessor.builder()
                .emailVerificationRepository(new FakeEmailVerificationRepository())
                .mailSender(new DummyEmailSender())
                .uuidHolder(new StubUuidHolder())
                .build();

        memberAuthenticationService = MemberAuthenticationService.builder()
                .memberRepository(new FakeMemberRepository())
                .passwordProcessor(new SpyPasswordProcessor())
                .randomHolder(new StubRandomHolder())
                .emailVerificationProcessor(emailVerificationProcessor)
                .build();
    }

    @Test
    @DisplayName("이메일로 회원가입해서 생성된 멤버의 상태값이 정상인지 체크한다.")
    void createMemberByEmail() {
        // given
        EmailSignup emailSignup = fixEmailSignup();

        // when
        Member member = memberAuthenticationService.createMemberByEmail(emailSignup);

        // then
        assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getMemberRole()).isEqualTo(MemberRole.USER);
        assertThat(member.getMemberAuthenticationInformation().getProviderType()).isEqualTo(ProviderType.EMAIL);

        assertThat(member.getMemberPrivateInformation().getEmail()).isEqualTo(emailSignup.getEmail());
        assertThat(member.getMemberPrivateInformation().getNickname()).isEqualTo(emailSignup.getNickname()+"#1234");
        assertThat(member.getMemberPrivateInformation().getAccountNumber()).isNull();
        assertThat(member.getMemberPrivateInformation().getPhoneNumber()).isNull();
        assertThat(member.getMemberPrivateInformation().getAccountName()).isNull();
        assertThat(member.getMemberPrivateInformation().getPoint()).isZero();

    }

    @Test
    void sendEmailForVerification() {
        // given
        Member member = createdMyEmail();

        // when
        EmailVerification emailVerification = memberAuthenticationService.sendEmailForVerification(member);

        // then
        assertThat(emailVerification.getEmailVerificationType()).isEqualTo(EmailVerificationType.REGISTER);
        assertThat(emailVerification.getMemberId()).isEqualTo(member.getId());
        assertThat(emailVerification.getEmailAddress()).isEqualTo(member.getMemberPrivateInformation().getEmail());
        assertThat(emailVerification.getVerificationCode()).isEqualTo("1234");
    }
}