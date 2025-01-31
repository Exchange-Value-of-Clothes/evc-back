package com.yzgeneration.evc.external.social;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverUserProfile implements SocialUserProfile<NaverUserProfile.Response>{

    private String resultcode;
    private String message;
    private Response response;

    @Getter
    @NoArgsConstructor
    public static class Response {
        private String id;
        private String nickname;
        private String mobile;
    }


    @Override
    public String getId() {
        return response.getId();
    }

    @Override
    public Response getResponse() {
        return response;
    }
}
