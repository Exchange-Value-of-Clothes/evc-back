package com.yzgeneration.evc.external.social;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;
import com.yzgeneration.evc.domain.member.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class SocialPlatformProvider {

    private final List<SocialLogin> socialLogins;

    public String getAuthorizationCode(String providerType, String state) {
        for (SocialLogin socialLogin : socialLogins) {
            if (socialLogin.getSocialPlatform().name().equals(providerType)) {
                return socialLogin.getAuthorizationCode(state);
            }
        }
        throw new CustomException(ErrorCode.SOCIAL_LOGIN);
    }

}
