package com.yzgeneration.evc.external.social;


import org.springframework.http.ResponseEntity;

public interface SocialLogin {

    ResponseEntity<Void> getAuthorizationCode(String state);
    String getToken(String authorizeCode, String state);
    SocialPlatform getSocialPlatform();
    SocialUserProfile getUserProfile(String accessToken);

}
