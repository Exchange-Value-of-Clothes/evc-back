package com.yzgeneration.evc.domain.member.authentication.implement;

import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import com.yzgeneration.evc.domain.member.service.port.PasswordProcessor;
import com.yzgeneration.evc.external.social.SocialLoginProcessor;
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
    private final SocialLoginProcessor socialLoginProcessor;

    public Member login(String email, String password) {
        Member member = memberRepository.getByEmail(email);
        member.checkPassword(password, passwordProcessor);
        member.checkStatus();
        return member;
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
}
