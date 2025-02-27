package com.yzgeneration.evc.authentication.service;

import com.yzgeneration.evc.authentication.dto.AuthenticationResponse;
import com.yzgeneration.evc.authentication.implement.AuthenticationProcessor;
import com.yzgeneration.evc.authentication.implement.TokenProvider;
import com.yzgeneration.evc.authentication.service.port.RefreshTokenRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import com.yzgeneration.evc.domain.member.service.port.PasswordProcessor;
import com.yzgeneration.evc.external.social.SocialLoginProcessor;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.mock.authentication.FakeRefreshTokenRepository;
import com.yzgeneration.evc.mock.external.SpyGoogleLogin;
import com.yzgeneration.evc.mock.member.FakeMemberRepository;
import com.yzgeneration.evc.mock.member.SpyPasswordProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor = new SpyPasswordProcessor();
    private static final String secret = "2VwKb97VKmrVskmUAedziOSJclcLxTO+xFiWZKh4vuE=";


    @BeforeEach
    void init() {
        memberRepository = new FakeMemberRepository();
        AuthenticationProcessor authenticationProcessor = new AuthenticationProcessor(memberRepository, passwordProcessor, new SocialLoginProcessor(List.of(new SpyGoogleLogin())));
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
        ResponseEntity<AuthenticationResponse.LoginResponse> response = authenticationService.login("ssar@naver.com", "12345678");
        // then
        assertThat(response.getBody()).hasFieldOrProperty("accessToken");
        assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE)).hasSize(1);
        assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).startsWith("refresh_token=");
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

    @Test
    @DisplayName("RefreshToken 으로 AccessToken, RefreshToken을 재발급 받을 수 있다.")
    void token_refresh_success() {
        // given
        Member member = MemberFixture.createdByEmail_ACTIVE();
        memberRepository.save(member);
        ResponseEntity<AuthenticationResponse.LoginResponse> loginResponse = authenticationService.login("ssar@naver.com", "12345678");
        String refreshToken="";
        String setCookieHeader = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        String[] cookies = setCookieHeader.split("; "); // 세미콜론(;) 기준으로 쿠키 분리
        for (String cookie : cookies) {
            if (cookie.startsWith("refresh_token=")) { // refresh_token 찾기
                refreshToken = cookie.substring("refresh_token=".length());
            }
        }
        // when
        ResponseEntity<AuthenticationResponse.RefreshResponse> response = authenticationService.refresh(refreshToken);

        // then
        assertThat(response.getBody()).hasFieldOrProperty("accessToken");
        assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE)).hasSize(1);
        assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0)).startsWith("refresh_token=");

    }

    @Test
    @DisplayName("적절한 ProviderType으로 인가코드를 요청할 수 있다.")
    void getAuthorizationCode() {
        // given
        // when
        ResponseEntity<Void> google = authenticationService.authorizationCode("GOOGLE", "1234");

        // then
        assertThat(google.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(google.getHeaders().get(HttpHeaders.LOCATION).get(0)).isEqualTo("https://www.google.com");

    }

    @Test
    @DisplayName("적절하지 못한 ProviderType은 예외를 던진다.")
    void InvalidProviderType() {
        // given
        // when
        // then
        assertThatThrownBy(() -> authenticationService.authorizationCode("INVALID", "1234"))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SOCIAL_LOGIN);

    }

    @Test
    @DisplayName("적절한 ProviderType, 인가코드, state로 소셜로그인을 성공한다.")
    void socialLogin() {
        // given
        authenticationService.authorizationCode("GOOGLE", "1234");
        // when
        ResponseEntity<AuthenticationResponse.LoginResponse> response = authenticationService.socialLogin("GOOGLE", "code", "1234");

        // then
        assertThat(response.getBody()).hasFieldOrProperty("accessToken");

    }

    @Test
    @DisplayName("적절하지 못한 state로 소셜로그인이 실패한다.")
    void socialLogin_failed() {
        // given
        authenticationService.authorizationCode("GOOGLE", "1234");
        // when
        // then
        assertThatThrownBy(() -> authenticationService.socialLogin("GOOGLE", "code", "12345"))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_CSRF);

    }


}