package com.yzgeneration.evc.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProfileResponse {
    private String imageName;
    private String imageUrl;
    private String nickname;
    private int point;
    private Boolean isSocialProfileVisible;
}
