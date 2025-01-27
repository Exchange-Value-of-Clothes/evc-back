package com.yzgeneration.evc.domain.member.dto;


import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;
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
        @NotBlank
        private String checkPassword;

        @Override
        public void valid() {
            EmailValidator.validate(email);
            PasswordValidator.validate(password);
            if(password.equals(checkPassword)) return;
            throw new CustomException(ErrorCode.INVALID_PASSWORD, "비밀번호와 비밀번호 확인이 다릅니다.");
        }
    }
}
