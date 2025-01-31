package com.yzgeneration.evc.domain.member.authentication.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yzgeneration.evc.validator.EmailValidator;
import com.yzgeneration.evc.validator.PasswordValidator;
import com.yzgeneration.evc.validator.Validatable;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationRequest {

    @Getter
    @NoArgsConstructor
    public static class LoginRequest implements Validatable {

        private String email;
        private String password;

        @JsonCreator
        public LoginRequest(@JsonProperty("email") String email, @JsonProperty("password") String password) {
            this.email = email;
            this.password = password;
            valid();
        }

        @Override
        public void valid() {
            EmailValidator.validate(email);
            PasswordValidator.validate(password);
        }
    }
}
