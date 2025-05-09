package com.yzgeneration.evc.mock.image;

import com.yzgeneration.evc.domain.image.infrastructure.repository.ProfileImageRepository;
import com.yzgeneration.evc.domain.image.model.ProfileImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StubProfileImageRepository implements ProfileImageRepository {

    private final List<ProfileImage> data = new ArrayList<>();
    private int saveCnt = 0;

    @Override
    public Optional<ProfileImage> findById(Long memberId) {
        return Optional.of(ProfileImage.builder().imageUrl("imageUrl").memberId(memberId).build());
    }

    @Override
    public ProfileImage save(ProfileImage profileImage) {
        saveCnt++;
        for (ProfileImage datum : data) {
            if (datum.getMemberId().equals(profileImage.getMemberId())) {
                data.remove(datum);
                break;
            }
        }
        data.add(profileImage);
        return profileImage;
    }

    public int getSaveCnt() {
        return saveCnt;
    }
}
