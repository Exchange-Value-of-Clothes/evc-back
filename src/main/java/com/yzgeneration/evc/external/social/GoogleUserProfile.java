package com.yzgeneration.evc.external.social;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GoogleUserProfile implements SocialUserProfile{

    private String sub;
    private String email;
    private String picture;
    private String name;

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
        return email;
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
