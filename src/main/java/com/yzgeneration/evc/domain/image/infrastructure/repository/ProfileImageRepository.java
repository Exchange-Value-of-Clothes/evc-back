package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.yzgeneration.evc.domain.image.model.ProfileImage;

import java.util.Optional;

public interface ProfileImageRepository {
    Optional<ProfileImage> findById(Long memberId);
    ProfileImage save(ProfileImage profileImage);
}
