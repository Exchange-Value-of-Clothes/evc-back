package com.yzgeneration.evc.external.social;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface SocialLogin {

    String getAuthorizationCode(String state);
    String getAccessToken(String authorizeCode, String state) throws JsonProcessingException;
    SocialPlatform getSocialPlatform();

}
