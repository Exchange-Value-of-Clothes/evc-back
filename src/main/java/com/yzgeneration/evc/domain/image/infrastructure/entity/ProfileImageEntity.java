package com.yzgeneration.evc.domain.image.infrastructure.entity;

import com.yzgeneration.evc.domain.image.model.ProfileImage;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "profile_images")
public class ProfileImageEntity {

    protected ProfileImageEntity() {}

    @Id
    private Long memberId;

    private String name;

    private String imageUrl;

    private ProfileImageEntity(Long memberId, String name, String imageUrl) {
        this.memberId = memberId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static ProfileImageEntity from(ProfileImage profileImage) {
        return new ProfileImageEntity(profileImage.getMemberId(), profileImage.getName(), profileImage.getImageUrl());
    }

    public ProfileImage toModel() {
        return ProfileImage.builder()
                .memberId(memberId)
                .name(name)
                .imageUrl(imageUrl)
                .build();
    }
}
