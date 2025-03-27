package com.yzgeneration.evc.domain.member.implement;

import com.yzgeneration.evc.domain.image.infrastructure.entity.ProfileImageEntity;
import com.yzgeneration.evc.domain.image.infrastructure.repository.ProfileImageRepository;
import com.yzgeneration.evc.domain.image.model.ProfileImage;
import com.yzgeneration.evc.domain.member.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberProfileReader {

    private final ProfileImageRepository profileImageRepository;

    public ProfileImage getOrCreate(String name, Long memberId) {
        return profileImageRepository.findById(memberId).orElseGet(()->
                profileImageRepository.save(ProfileImageEntity.from(ProfileImage.of(memberId, name, null)).toModel())
        );
    }
}
