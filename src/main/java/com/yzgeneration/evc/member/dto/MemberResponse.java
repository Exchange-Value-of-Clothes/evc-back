package com.yzgeneration.evc.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @AllArgsConstructor
    public static class RegisterResponse {
        private String verificationToken;
    }
}
