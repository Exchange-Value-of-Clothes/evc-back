package com.yzgeneration.evc.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.validator.PasswordValidator;
import com.yzgeneration.evc.validator.Validatable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePassword implements Validatable {
    private String oldPassword;
    private String newPassword;
    private String checkPassword;

    @JsonCreator
    public ChangePassword(@JsonProperty("oldPassword") String oldPassword,
                          @JsonProperty("newPassword") String newPassword,
                          @JsonProperty("checkPassword") String checkPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.checkPassword = checkPassword;
    }

    @Override
    public void valid() {
        PasswordValidator.validate(newPassword);
        if(!newPassword.equals(checkPassword)) throw new CustomException(ErrorCode.INVALID_PASSWORD, "비밀번호와 비밀번호 확인이 다릅니다.");
    }
}
