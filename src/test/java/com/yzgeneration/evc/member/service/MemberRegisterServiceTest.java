package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.member.dto.MemberRequest.EmailSignup;
import com.yzgeneration.evc.member.enums.MemberRole;
import com.yzgeneration.evc.member.enums.MemberStatus;
import com.yzgeneration.evc.member.enums.ProviderType;
import com.yzgeneration.evc.member.implement.MemberCreator;
import com.yzgeneration.evc.member.implement.MemberValidator;
import com.yzgeneration.evc.member.model.Member;
import com.yzgeneration.evc.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.member.service.port.MemberRepository;
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

import static com.yzgeneration.evc.common.exception.ErrorCode.*;
import static com.yzgeneration.evc.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

class MemberRegisterServiceTest {

    private MemberRegisterService memberRegisterService;
    private MemberValidator memberValidator;

    @BeforeEach
    void init() {
        EmailVerificationProcessor emailVerificationProcessor = new EmailVerificationProcessor(
                new FakeEmailVerificationRepository(),
                new StubUuidHolder(),
                new DummyEmailSender());

        MemberRepository memberRepository = new FakeMemberRepository();
        memberValidator = new MemberValidator(memberRepository);


        MemberCreator memberCreator = new MemberCreator(
                        memberRepository,
                        new SpyPasswordProcessor(),
                        new StubRandomHolder(),
                        memberValidator);

        memberRegisterService = new MemberRegisterService(memberCreator, emailVerificationProcessor);
    }

    @Test
    @DisplayName("이메일로 회원가입해서 생성된 멤버의 상태값이 정상인지 체크한다.")
    void createMemberByEmail() {
        // given
        EmailSignup emailSignup = fixEmailSignup();

        // when
        Member member = memberRegisterService.createMemberByEmail(emailSignup);

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
    @DisplayName("이미 가입된 이메일이 있다면 예외를 던진다.")
    public void existedEmailThrowException() {
        // given
        EmailSignup emailSignup = fixEmailSignup();
        memberRegisterService.createMemberByEmail(emailSignup);

        // when
        // then
        assertThatThrownBy(() -> memberRegisterService.createMemberByEmail(emailSignup))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", EMAIL_DUPLICATION);
    }

    @Test
    @DisplayName("인증 요청을 위해 이메일을 전송할 수 있다.")
    void sendEmailForRequestVerification() {
        // given
        Member member = createdByEmail();

        // when
        EmailVerification emailVerification = memberRegisterService.sendEmailForRequestVerification(member);

        // then
        assertThat(emailVerification.getEmailVerificationType()).isEqualTo(EmailVerificationType.REGISTER);
        assertThat(emailVerification.getMemberId()).isEqualTo(member.getId());
        assertThat(emailVerification.getEmailAddress()).isEqualTo(member.getMemberPrivateInformation().getEmail());
        assertThat(emailVerification.getVerificationCode()).isEqualTo("1234");
    }

    @Test
    @DisplayName("인증코드를 통해 인증을 할 수 있다.")
    void verify() {
        // given
        Member member = createdByEmail();
        memberRegisterService.sendEmailForRequestVerification(member);
        // when
        memberRegisterService.verify("1234");
    }

    @Test
    @DisplayName("인증코드가 다르면 예외를 던진다.")
    void verifyFailedThrowException() {
        // given
        Member member = createdByEmail();
        memberRegisterService.sendEmailForRequestVerification(member);
        // when
        assertThatThrownBy(() -> memberRegisterService.verify("12345"))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", EMAIL_VERIFICATION_NOT_FOUND);

    }

    @Test
    @DisplayName("인증코드를 재전송 할 수 있다.")
    void resendVerificationCode() {
        // given
        String email = "ssar@naver.com";
        EmailSignup emailSignup = fixEmailSignup();
        Member member = memberRegisterService.createMemberByEmail(emailSignup);
        memberRegisterService.sendEmailForRequestVerification(member);

        // when
        // then
        memberRegisterService.resendVerificationCode(email);

    }

    @Test
    @DisplayName("존재하지 않는 이메일은 인증코드를 재전송 할 수 없다.")
    void resendVerificationCodeFailed() {
        // given
        String email = "cos@naver.com";
        EmailSignup emailSignup = fixEmailSignup();
        Member member = memberRegisterService.createMemberByEmail(emailSignup);
        memberRegisterService.sendEmailForRequestVerification(member);

        // when
        // then
        assertThatThrownBy(() -> memberRegisterService.resendVerificationCode(email))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", EMAIL_VERIFICATION_NOT_FOUND);

    }

}