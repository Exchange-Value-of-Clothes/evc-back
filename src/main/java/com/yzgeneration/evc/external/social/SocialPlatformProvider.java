package com.yzgeneration.evc.external.social;

import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class SocialPlatformProvider {

    private final List<SocialLogin> socialLogins;

    public ResponseEntity<Void> getAuthorizationCode(String providerType, String state) {
        for (SocialLogin socialLogin : socialLogins) {
            if (socialLogin.getSocialPlatform().name().equals(providerType)) {
                return socialLogin.getAuthorizationCode(state);
            }
        }
        throw new CustomException(ErrorCode.SOCIAL_LOGIN);
    }

    public String getAccessToken(String providerType, String authorizationCode, String state) {
        for (SocialLogin socialLogin : socialLogins) {
            if (socialLogin.getSocialPlatform().name().equals(providerType)) {
                return socialLogin.getAccessToken(authorizationCode, state);
            }
        }
        throw new CustomException(ErrorCode.SOCIAL_LOGIN);
    }

    public SocialUserProfile<?> getUserProfile(String providerType, String accessToken) {
        for (SocialLogin socialLogin : socialLogins) {
            if (socialLogin.getSocialPlatform().name().equals(providerType)) {
                return socialLogin.getUserProfile(accessToken);
            }
        }
        throw new CustomException(ErrorCode.SOCIAL_LOGIN);
    }

}
