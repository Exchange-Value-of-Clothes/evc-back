package com.yzgeneration.evc.mock.external;

import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.external.social.GoogleUserProfile;
import com.yzgeneration.evc.external.social.SocialLogin;
import com.yzgeneration.evc.external.social.SocialPlatform;
import com.yzgeneration.evc.external.social.SocialUserProfile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SpyGoogleLogin implements SocialLogin {

    private String state;

    @Override
    public ResponseEntity<Void> getAuthorizationCode(String state) {
        this.state = state;
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "https://www.google.com").build();
    }

    @Override
    public String getToken(String authorizeCode, String state)  {
        if (state.equals(this.state)) {
            return "id_token";
        }
        throw new CustomException(ErrorCode.INVALID_CSRF);
    }

    @Override
    public SocialPlatform getSocialPlatform() {
        return SocialPlatform.GOOGLE;
    }

    @Override
    public SocialUserProfile getUserProfile(String accessToken) {
        return new GoogleUserProfile("sub", "email", "picture", "name");
    }
}
