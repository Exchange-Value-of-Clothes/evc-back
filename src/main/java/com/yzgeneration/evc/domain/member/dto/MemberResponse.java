package com.yzgeneration.evc.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @AllArgsConstructor
    public static class RegisterResponse {
        private String verificationCode;
    }

    @Getter
    @AllArgsConstructor
    public static class ResendCodeResponse {
        private String verificationCode;
    }
}
