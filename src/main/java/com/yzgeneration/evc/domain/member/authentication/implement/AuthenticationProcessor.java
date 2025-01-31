package com.yzgeneration.evc.domain.member.authentication.implement;

import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import com.yzgeneration.evc.domain.member.service.port.PasswordProcessor;
import com.yzgeneration.evc.external.social.SocialPlatformProvider;
import com.yzgeneration.evc.external.social.SocialUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {

    private final MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor;
    private final SocialPlatformProvider socialPlatformProvider;

    public Member login(String email, String password) {
        Member member = memberRepository.getByEmail(email);
        member.checkPassword(password, passwordProcessor);
        member.checkStatus();
        return member;
    }

    public ResponseEntity<Void> getAuthorizationCode(String providerType, String state) {
        return socialPlatformProvider.getAuthorizationCode(providerType, state);
    }

    public Member socialLogin(String providerType, String authorizationCode, String state) {
        String accessToken = socialPlatformProvider.getAccessToken(providerType, authorizationCode, state);
        SocialUserProfile<?> socialUserProfile = socialPlatformProvider.getUserProfile(providerType, accessToken);
        Optional<Member> socialMember = memberRepository.findSocialMember(providerType, socialUserProfile.getId());
        return socialMember.orElseGet(() -> memberRepository.save(Member.socialLogin(providerType, socialUserProfile)));
    }
}
