package com.yzgeneration.evc.mock.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yzgeneration.evc.external.social.SocialLogin;
import com.yzgeneration.evc.external.social.SocialPlatform;

public class FakeSocialLogin implements SocialLogin {
    @Override
    public String getAuthorizationCode(String state) {
        return "";
    }

    @Override
    public String getAccessToken(String authorizeCode, String state) throws JsonProcessingException {
        return "";
    }

    @Override
    public SocialPlatform getSocialPlatform() {
        return null;
    }
}
