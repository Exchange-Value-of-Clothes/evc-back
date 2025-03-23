package com.yzgeneration.evc.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyInformationResponse {
    private String profileImage;
    private String nickname;
    private int point;
}
