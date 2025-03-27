package com.yzgeneration.evc.mock.image;

import com.yzgeneration.evc.domain.image.infrastructure.repository.ProfileImageRepository;
import com.yzgeneration.evc.domain.image.model.ProfileImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeProfileImageRepository implements ProfileImageRepository {

    private final List<ProfileImage> data = new ArrayList<>();

    @Override
    public Optional<ProfileImage> findById(Long memberId) {
        for (ProfileImage datum : data) {
            if(datum.getMemberId().equals(memberId)) return Optional.of(datum);
        }
        return Optional.empty();
    }

    @Override
    public ProfileImage save(ProfileImage profileImage) {
        for (ProfileImage datum : data) {
            if (datum.getMemberId().equals(profileImage.getMemberId())) {
                data.remove(datum);
                break;
            }
        }
        data.add(profileImage);
        return profileImage;
    }
}
