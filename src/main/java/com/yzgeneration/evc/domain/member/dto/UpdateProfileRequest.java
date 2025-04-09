package com.yzgeneration.evc.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yzgeneration.evc.domain.member.implement.MemberValidator;
import com.yzgeneration.evc.validator.Validatable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileRequest implements Validatable {

    private String nickname;
    private String imageName;

    @JsonCreator
    public UpdateProfileRequest(@JsonProperty("nickname") String nickname, @JsonProperty("imageName") String imageName) {
        this.nickname = nickname;
        this.imageName = imageName;
        valid();
    }

    @Override
    public void valid() {
        MemberValidator.nickname(nickname);
    }
}
