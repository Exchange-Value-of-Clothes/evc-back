package com.yzgeneration.evc.member.dto;

import lombok.Getter;

public class MemberRequest {

    @Getter
    public static class MemberEmailCreate {
        private String nickname;
        private String email;
        private String password;
        private String checkPassword;
    }
}
