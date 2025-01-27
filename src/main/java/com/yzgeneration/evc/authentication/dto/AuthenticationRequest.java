package com.yzgeneration.evc.authentication.dto;

import com.yzgeneration.evc.common.validator.EmailValidator;
import com.yzgeneration.evc.common.validator.PasswordValidator;
import com.yzgeneration.evc.common.validator.Validatable;
import lombok.Getter;

public class AuthenticationRequest {

    @Getter
    public static class LoginRequest implements Validatable {

        private String email;
        private String password;

        @Override
        public void valid() {
            EmailValidator.validate(email);
            PasswordValidator.validate(password);
        }
    }
}
