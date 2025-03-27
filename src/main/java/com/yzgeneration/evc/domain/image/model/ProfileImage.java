package com.yzgeneration.evc.domain.image.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileImage {

    private Long memberId;
    private String name;
    private String imageUrl;

    public static ProfileImage of(Long memberId, String name, String imageUrl) {
        return ProfileImage.builder().memberId(memberId).name(name).imageUrl(imageUrl).build();
    }
}
