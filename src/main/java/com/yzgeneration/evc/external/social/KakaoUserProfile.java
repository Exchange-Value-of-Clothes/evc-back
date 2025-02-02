package com.yzgeneration.evc.external.social;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KakaoUserProfile implements SocialUserProfile {

    private String sub;
    private String name;
    private String picture;

    @Override
    public String getProviderId() {
        return sub;
    }

    @Override
    public String getNickname() {
        return name;
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getPhoneNumber() {
        return "";
    }

    @Override
    public String getProfileImage() {
        return picture;
    }

}
