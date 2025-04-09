package com.yzgeneration.evc.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yzgeneration.evc.validator.EmailValidator;
import com.yzgeneration.evc.validator.Validatable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeEmail implements Validatable {

    private String email;

    @JsonCreator
    public ChangeEmail(@JsonProperty("email") String email) {
        this.email = email;
        valid();
    }

    @Override
    public void valid() {
        EmailValidator.validate(email);
    }
}
