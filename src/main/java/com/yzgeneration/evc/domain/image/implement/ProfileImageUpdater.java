package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.infrastructure.repository.ProfileImageRepository;
import com.yzgeneration.evc.domain.image.model.ProfileImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileImageUpdater {

    private final ProfileImageRepository profileImageRepository;

    public ProfileImage update(String imageName, Long memberId) {
        ProfileImage profileImage = profileImageRepository.findById(memberId).orElseGet(() -> profileImageRepository.save(ProfileImage.create(memberId, imageName, null)));
        if (imageName.isBlank() && profileImage.isEmpty()) return profileImage;
        profileImage.update(imageName);
        return profileImageRepository.save(profileImage);
    }
}
