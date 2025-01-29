package com.yzgeneration.evc.authentication.service;

import com.yzgeneration.evc.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.authentication.implement.AuthenticationProcessor;
import com.yzgeneration.evc.authentication.implement.TokenProvider;
import com.yzgeneration.evc.authentication.service.port.RefreshTokenRepository;
import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import com.yzgeneration.evc.domain.member.service.port.PasswordProcessor;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.mock.authentication.FakeRefreshTokenRepository;
import com.yzgeneration.evc.mock.member.FakeMemberRepository;
import com.yzgeneration.evc.mock.member.SpyPasswordProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private MemberRepository memberRepository;


    @BeforeEach
    void init() {
        memberRepository = new FakeMemberRepository();
        PasswordProcessor passwordProcessor = new SpyPasswordProcessor();
        AuthenticationProcessor authenticationProcessor = new AuthenticationProcessor(memberRepository, passwordProcessor);
        String secret = "2VwKb97VKmrVskmUAedziOSJclcLxTO+xFiWZKh4vuE=";
        RefreshTokenRepository refreshTokenRepository = new FakeRefreshTokenRepository();
        TokenProvider tokenProvider = new TokenProvider(secret, refreshTokenRepository);
        authenticationService = new AuthenticationService(authenticationProcessor, tokenProvider);
    }

    @Test
    @DisplayName("ACTIVE 상태 Member는 로그인을 할 수 있다.")
    void login_success() {
        // given
        Member member = MemberFixture.createdByEmail_ACTIVE();
        memberRepository.save(member);
        // when
        AuthenticationToken authenticationToken = authenticationService.login("ssar@naver.com", "12345678");
        // then
        assertThat(authenticationToken).hasFieldOrProperty("accessToken");
        assertThat(authenticationToken).hasFieldOrProperty("refreshToken");
    }

    @Test
    @DisplayName("ACTIVE 상태가 아닌 Member는 로그인을 할 수 없다.")
    void login_fail_inactive_member() {
        // given
        Member member = MemberFixture.createdByEmail_PENDING();
        memberRepository.save(member);
        // when
        // then
        assertThatThrownBy(() -> authenticationService.login("ssar@naver.com", "12345678"))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INACTIVE_MEMBER);
    }

    @Test
    @DisplayName("잘못된 이메일로 로그인을 할 수 없다.")
    void login_fail_incorrect_email() {
        // given
        Member member = MemberFixture.createdByEmail_ACTIVE();
        memberRepository.save(member);
        // when
        // then
        assertThatThrownBy(() -> authenticationService.login("cos@naver.com", "12345678"))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인을 할 수 없다.")
    void login_fail_incorrect_password() {
        // given
        Member member = MemberFixture.createdByEmail_ACTIVE();
        memberRepository.save(member);
        // when
        // then
        assertThatThrownBy(() -> authenticationService.login("ssar@naver.com", "999"))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.LOGIN_FAILED);
    }
}