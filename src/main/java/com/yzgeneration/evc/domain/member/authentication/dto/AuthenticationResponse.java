package com.yzgeneration.evc.domain.member.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthenticationResponse {

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
        private AuthenticationToken authenticationToken;
    }

    @Getter
    @AllArgsConstructor
    public static class RefreshResponse {
        private AuthenticationToken authenticationToken;
    }
}
