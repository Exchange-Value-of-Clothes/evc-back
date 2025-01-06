package com.yzgeneration.evc.member.dto;


import com.yzgeneration.evc.common.validator.EmailValidator;
import com.yzgeneration.evc.common.validator.PasswordValidator;
import com.yzgeneration.evc.common.validator.Validatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class MemberRequest {

    @Getter
    public static class EmailSignup implements Validatable {

        @Size(min = 2)
        @NotBlank
        private String nickname;
        private String email;
        private String password;
        private String checkPassword;

        @Override
        public void valid() {
            EmailValidator.validate(email);
            PasswordValidator.validate(password);
        }
    }
}
