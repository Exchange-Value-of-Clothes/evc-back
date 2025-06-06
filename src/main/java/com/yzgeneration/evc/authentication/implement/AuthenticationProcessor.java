package com.yzgeneration.evc.authentication.implement;

import com.yzgeneration.evc.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.MemberCreatedEvent;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.infrastructure.MemberRepository;
import com.yzgeneration.evc.domain.member.infrastructure.PasswordProcessor;
import com.yzgeneration.evc.external.social.SocialLoginProcessor;
import com.yzgeneration.evc.external.social.SocialUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.yzgeneration.evc.authentication.dto.AuthenticationResponse.*;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {

    private final MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor;
    private final SocialLoginProcessor socialLoginProcessor;
    private final ApplicationEventPublisher eventPublisher;

    public Member login(String email, String password) {
        Member member = memberRepository.getByEmail(email);
        member.checkPassword(password, passwordProcessor);
        member.checkStatus();
        return member;
    }

    public ResponseEntity<LoginResponse> getLoginResponse(AuthenticationToken authenticationToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", authenticationToken.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("None") // Strict : 완전한 크로스 사이트 차단, Lax (기본값) → GET 요청 + 리다이렉트 허용, 하지만 POST 요청 차단 None : 교차 도메인 요청에 전송 가능(단, secure=true (https 에만 전송 가능))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(authenticationToken.getAccessToken()));
    }

    public ResponseEntity<RefreshResponse> getRefreshResponse(AuthenticationToken authenticationToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", authenticationToken.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("None")
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
        return socialMember.orElseGet(() -> {
            Member member = memberRepository.save(Member.socialLogin(providerType, socialUserProfile));
            eventPublisher.publishEvent(new MemberCreatedEvent(member.getId(), socialUserProfile.getProfileImage()));
            return member;
        });
    }

    public ResponseEntity<LoginResponse> getSocialLoginResponse(AuthenticationToken authenticationToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", authenticationToken.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.LOCATION, "https://d1vw7pjo2v9aef.cloudfront.net/social-login-success")
                .body(new LoginResponse(authenticationToken.getAccessToken()));
    }

    public ResponseEntity<Void> deleteRefreshToken(String refreshToken) {
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 삭제를 위해 만료 시간을 0으로 설정
                .sameSite("None")
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }
}
