package com.yzgeneration.evc.domain.member.service;

import com.yzgeneration.evc.domain.image.implement.ProfileImageUpdater;
import com.yzgeneration.evc.domain.image.model.ProfileImage;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.dto.UpdateProfileRequest;
import com.yzgeneration.evc.domain.member.dto.UpdateProfileResponse;
import com.yzgeneration.evc.domain.member.implement.MemberUpdater;
import com.yzgeneration.evc.domain.member.infrastructure.MemberProfileRepository;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.point.infrastructure.MemberPointRepository;
import com.yzgeneration.evc.domain.point.model.MemberPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;
    private final MemberUpdater memberUpdater;
    private final ProfileImageUpdater profileImageUpdater ;
    private final MemberPointRepository memberPointRepository;

    public ProfileResponse get(Long memberId) {
        return memberProfileRepository.getMyProfile(memberId);
    }

    public UpdateProfileResponse update(UpdateProfileRequest updateProfileRequest, Long memberId) {
        ProfileImage profileImage = profileImageUpdater.update(updateProfileRequest.getImageName(), memberId);
        Member member = memberUpdater.changeNickname(memberId, updateProfileRequest.getNickname());
        MemberPoint memberPoint = memberPointRepository.getById(memberId);
        return new UpdateProfileResponse(profileImage.getName(), profileImage.getImageUrl(), member.getMemberPrivateInformation().getNickname(), memberPoint.getPoint(), profileImage.getIsSocialProfileVisible());
    }

}
