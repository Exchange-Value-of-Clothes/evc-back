package com.yzgeneration.evc.external.social;

import lombok.Getter;

@Getter
public class SocialUserInformation {

    private SocialPlatform socialPlatform;
    private String providerId;
    private String nickName;
    private String email;
    private String phoneNumber;

}
