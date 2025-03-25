package com.yzgeneration.evc.member.model;


import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.domain.member.enums.MemberRole;
import com.yzgeneration.evc.domain.member.enums.MemberStatus;
import com.yzgeneration.evc.domain.member.enums.ProviderType;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.domain.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.domain.member.infrastructure.PasswordProcessor;
import com.yzgeneration.evc.mock.member.SpyPasswordProcessor;
import com.yzgeneration.evc.mock.StubRandomHolder;
import com.yzgeneration.evc.common.implement.port.RandomHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.yzgeneration.evc.exception.ErrorCode.*;
import static com.yzgeneration.evc.domain.member.dto.MemberRequest.*;
import static com.yzgeneration.evc.fixture.MemberFixture.fixtureMonkey;
import static org.assertj.core.api.Assertions.*;

class MemberTest {

    private final RandomHolder randomHolder = new StubRandomHolder();
    private final PasswordProcessor passwordProcessor = new SpyPasswordProcessor();

    @Test
    @DisplayName("회원의 개인정보를 이메일을 통해 구성할 수 있다.")
    void memberPrivateInformationCreatedByEmail() {
        // given
        EmailSignup emailSignup = fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssar@naver.com")
                .set("nickname", "구코딩")
                .sample();

        // when
        MemberPrivateInformation memberPrivateInformation = MemberPrivateInformation.createdByEmail(emailSignup.getNickname(), emailSignup.getEmail(), randomHolder);

        // then
        assertThat(memberPrivateInformation.getNickname()).isEqualTo("구코딩#1234");
        assertThat(memberPrivateInformation.getEmail()).isEqualTo("ssar@naver.com");
        assertThat(memberPrivateInformation.getPoint()).isZero();
        assertThat(memberPrivateInformation.getAccountName()).isNull();
        assertThat(memberPrivateInformation.getAccountNumber()).isNull();
        assertThat(memberPrivateInformation.getPhoneNumber()).isNull();
    }

    @Test
    @DisplayName("잘못된 이메일 형식은 예외을 발생시킨다.")
    void wrongEmailThrowsException() {
        // given
        EmailSignup emailSignup = fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssarnaver.com")
                .set("nickname", "구코딩")
                .sample();

        // when
        // then
        assertThatThrownBy(emailSignup::valid)
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", INVALID_EMAIL);

    }

    @Test
    @DisplayName("회원의 인증정보를 이메일을 통해 구성할 수 있다.")
    void memberAuthenticationInformationCreatedByEmail() {
        // given
        EmailSignup emailSignup = fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssar@naver.com")
                .set("nickname", "구코딩")
                .set("password", "12345678")
                .set("checkPassword", "12345678")
                .sample();

        // when
        MemberAuthenticationInformation memberAuthenticationInformation = MemberAuthenticationInformation.createdByEmail(emailSignup.getPassword(), passwordProcessor);

        // then
        assertThat(memberAuthenticationInformation.getPassword()).isEqualTo("12345678");
        assertThat(memberAuthenticationInformation.getProviderType()).isEqualTo(ProviderType.EMAIL);
        assertThat(memberAuthenticationInformation.getProviderId()).isNull();
    }

    @Test
    @DisplayName("비밀번호와 비밀번호확인 값이 다르면 예외를 발생시킨다.")
    void passwordValidThrowsException() {
        // given
        EmailSignup emailSignup = fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssar@naver.com")
                .set("nickname", "구코딩")
                .set("password", "12345678")
                .set("checkPassword", "12345679")
                .sample();

        // when
        // then
        assertThatThrownBy(emailSignup::valid)
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", INVALID_PASSWORD);
    }

    @Test
    @DisplayName("Member를 생성할 수 있다.")
    void createMember() {
        // given
        EmailSignup emailSignup = fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssar@naver.com")
                .set("nickname", "구코딩")
                .set("password", "12345678")
                .set("checkPassword", "12345678")
                .sample();
        MemberPrivateInformation memberPrivateInformation = MemberPrivateInformation.createdByEmail(emailSignup.getNickname(), emailSignup.getEmail(), randomHolder);
        MemberAuthenticationInformation memberAuthenticationInformation = MemberAuthenticationInformation.createdByEmail(emailSignup.getPassword(), passwordProcessor);


        // when
        Member member = Member.create(memberPrivateInformation, memberAuthenticationInformation, MemberRole.USER, MemberStatus.PENDING);

        // then
        assertThat(member.getId()).isNull();
        assertThat(member.getMemberRole()).isEqualTo(MemberRole.USER);
        assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getMemberPrivateInformation()).isNotNull();
        assertThat(member.getMemberAuthenticationInformation()).isNotNull();

    }

    @Test
    @DisplayName("Member의 상태를 Active로 변경할 수 있다.")
    void activateMember() {
        // given
        EmailSignup emailSignup = fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssar@naver.com")
                .set("nickname", "구코딩")
                .set("password", "12345678")
                .set("checkPassword", "12345678")
                .sample();
        MemberPrivateInformation memberPrivateInformation = MemberPrivateInformation.createdByEmail(emailSignup.getNickname(), emailSignup.getEmail(), randomHolder);
        MemberAuthenticationInformation memberAuthenticationInformation = MemberAuthenticationInformation.createdByEmail(emailSignup.getPassword(), passwordProcessor);
        Member member = Member.create(memberPrivateInformation, memberAuthenticationInformation, MemberRole.USER, MemberStatus.PENDING);

        // when
        member.active();

        // then
        assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("Member의 상태가 ACTIVE 인지 확인할 수 있다.")
    void checkStatus() {
        // given
        EmailSignup emailSignup = fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssar@naver.com")
                .set("nickname", "구코딩")
                .set("password", "12345678")
                .set("checkPassword", "12345678")
                .sample();
        MemberPrivateInformation memberPrivateInformation = MemberPrivateInformation.createdByEmail(emailSignup.getNickname(), emailSignup.getEmail(), randomHolder);
        MemberAuthenticationInformation memberAuthenticationInformation = MemberAuthenticationInformation.createdByEmail(emailSignup.getPassword(), passwordProcessor);
        Member member = Member.create(memberPrivateInformation, memberAuthenticationInformation, MemberRole.USER, MemberStatus.ACTIVE);

        // when
        // then
        member.checkStatus();
    }

    @Test
    @DisplayName("Member의 상태가 Active가 아니면 예외를 던진다.")
    void notActive() {
        // given
        EmailSignup emailSignup = fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssar@naver.com")
                .set("nickname", "구코딩")
                .set("password", "12345678")
                .set("checkPassword", "12345678")
                .sample();
        MemberPrivateInformation memberPrivateInformation = MemberPrivateInformation.createdByEmail(emailSignup.getNickname(), emailSignup.getEmail(), randomHolder);
        MemberAuthenticationInformation memberAuthenticationInformation = MemberAuthenticationInformation.createdByEmail(emailSignup.getPassword(), passwordProcessor);
        Member member = Member.create(memberPrivateInformation, memberAuthenticationInformation, MemberRole.USER, MemberStatus.PENDING);

        // when
        // then
        assertThatThrownBy(member::checkStatus).
                isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("ErrorCode", INACTIVE_MEMBER);
    }
}