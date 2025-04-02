package com.yzgeneration.evc.domain.image.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileImage {

    private Long memberId;
    private String name;
    private String imageUrl;
    private Boolean isSocialProfileVisible;

    public static ProfileImage create(Long memberId, String name, String imageUrl) {
        return ProfileImage.builder().memberId(memberId).name(name).imageUrl(imageUrl).isSocialProfileVisible(true).build();
    }

    public void update(String name) {
        this.name = name;
    }

    public boolean isEmpty() {
        return this.imageUrl.isEmpty() && this.name.isEmpty();
    }
}
