package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.yzgeneration.evc.domain.image.infrastructure.entity.ProfileImageEntity;
import com.yzgeneration.evc.domain.image.model.ProfileImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileImageRepositoryImpl implements ProfileImageRepository {

    private final ProfileImageJpaRepository profileImageJpaRepository;

    @Override
    public Optional<ProfileImage> findById(Long memberId) {
         return profileImageJpaRepository.findById(memberId).map(ProfileImageEntity::toModel);
    }

    @Override
    public ProfileImage save(ProfileImage profileImage) {
        return profileImageJpaRepository.save(ProfileImageEntity.from(profileImage)).toModel();
    }
}
