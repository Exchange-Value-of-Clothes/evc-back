package com.yzgeneration.evc.domain.member.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yzgeneration.evc.domain.member.implement.MemberValidator;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.validator.EmailValidator;
import com.yzgeneration.evc.validator.PasswordValidator;
import com.yzgeneration.evc.validator.Validatable;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {

    @Getter
    @NoArgsConstructor
    public static class EmailSignup implements Validatable {

        private String nickname;
        private String email;
        private String password;
        private String checkPassword;

        @JsonCreator
        public EmailSignup(@JsonProperty("nickname") String nickname, @JsonProperty("email") String email,
                           @JsonProperty("password") String password, @JsonProperty("checkPassword") String checkPassword) {

            this.nickname = nickname;
            this.email = email;
            this.password = password;
            this.checkPassword = checkPassword;
            valid();
        }

        @Override
        public void valid() {
            EmailValidator.validate(email);
            PasswordValidator.validate(password);
            MemberValidator.nickname(nickname);
            if(password.equals(checkPassword)) return;
            throw new CustomException(ErrorCode.INVALID_PASSWORD, "비밀번호와 비밀번호 확인이 다릅니다.");
        }
    }

    @Getter
    @NoArgsConstructor
    public static class RefreshRequest {
        private String refreshToken;
    }
}
