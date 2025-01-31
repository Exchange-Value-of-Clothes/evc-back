package com.yzgeneration.evc.mock.external;

import com.yzgeneration.evc.external.social.SocialLogin;
import com.yzgeneration.evc.external.social.SocialPlatform;
import com.yzgeneration.evc.external.social.SocialUserProfile;
import org.springframework.http.ResponseEntity;

public class FakeSocialLogin implements SocialLogin {
    @Override
    public ResponseEntity<Void> getAuthorizationCode(String state) {
        return null;
    }

    @Override
    public String getAccessToken(String authorizeCode, String state)  {
        return "";
    }

    @Override
    public SocialPlatform getSocialPlatform() {
        return null;
    }

    @Override
    public SocialUserProfile<?> getUserProfile(String accessToken) {
        return null;
    }
}
