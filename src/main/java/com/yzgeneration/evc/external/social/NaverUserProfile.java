package com.yzgeneration.evc.external.social;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverUserProfile implements SocialUserProfile{

    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @NoArgsConstructor
    private static class Response {
        private String id;
        private String nickname;
        private String email;
        private String mobile;
        private String profile_image;
    }

    @Override
    public String getProviderId() {
        return response.id;
    }

    @Override
    public String getNickname() {
        return response.nickname;
    }

    @Override
    public String getEmail() {
        return response.email;
    }

    @Override
    public String getPhoneNumber() {
        return response.mobile;
    }

    @Override
    public String getProfileImage() {
        return response.profile_image;
    }


}
