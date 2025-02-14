package com.yzgeneration.evc.domain.member.authentication.implement;

import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import com.yzgeneration.evc.domain.member.service.port.PasswordProcessor;
import com.yzgeneration.evc.external.social.SocialLoginProcessor;
import com.yzgeneration.evc.external.social.SocialUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationResponse.*;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {

    private final MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor;
    private final SocialLoginProcessor socialLoginProcessor;

    public Member login(String email, String password) {
        Member member = memberRepository.getByEmail(email);
        member.checkPassword(password, passwordProcessor);
        member.checkStatus();
        return member;
    }

    // TODO 도메인 설정, tsl 인증서
    public ResponseEntity<LoginResponse> getLoginResponse(AuthenticationToken authenticationToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", authenticationToken.getRefreshToken())
                .httpOnly(true)
                .secure(false) // TODO true
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("Lax") // Strict : 완전한 크로스 사이트 차단, Lax (기본값) → GET 요청 + 리다이렉트 허용, 하지만 POST 요청 차단 None : 교차 도메인 요청에 전송 가능(단, secure=true (https 에만 전송 가능))

                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(authenticationToken.getAccessToken()));
    }

    public ResponseEntity<RefreshResponse> getRefreshResponse(AuthenticationToken authenticationToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", authenticationToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new RefreshResponse(authenticationToken.getAccessToken()));
    }

    public ResponseEntity<Void> getAuthorizationCode(String providerType, String state) {
        return socialLoginProcessor.getAuthorizationCode(providerType, state);
    }

    public Member socialLogin(String providerType, String authorizationCode, String state) {
        String accessToken = socialLoginProcessor.getAccessToken(providerType, authorizationCode, state);
        SocialUserProfile socialUserProfile = socialLoginProcessor.getUserProfile(providerType, accessToken);
        Optional<Member> socialMember = memberRepository.findSocialMember(providerType, socialUserProfile.getProviderId());
        return socialMember.orElseGet(() -> memberRepository.save(Member.socialLogin(providerType, socialUserProfile)));
    }

    public ResponseEntity<LoginResponse> getSocialLoginResponse(AuthenticationToken authenticationToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", authenticationToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.LOCATION, "http://localhost:3000/social-login-success")
                .body(new LoginResponse(authenticationToken.getAccessToken()));
    }
}
