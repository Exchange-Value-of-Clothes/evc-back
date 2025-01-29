package com.yzgeneration.evc.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthenticationResponse {

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
        private AuthenticationToken authenticationToken;
    }
}
