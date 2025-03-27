package com.yzgeneration.evc.domain.member.service;

import com.yzgeneration.evc.domain.image.infrastructure.repository.ProfileImageRepository;
import com.yzgeneration.evc.domain.image.model.ProfileImage;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.dto.UpdateProfileRequest;
import com.yzgeneration.evc.domain.member.implement.MemberUpdater;
import com.yzgeneration.evc.domain.member.infrastructure.MemberProfileRepository;
import com.yzgeneration.evc.domain.member.infrastructure.MemberRepository;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.point.infrastructure.MemberPointRepository;
import com.yzgeneration.evc.domain.point.model.MemberPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;
    private final MemberUpdater memberUpdater;
    private final ProfileImageRepository profileImageRepository;
    private final MemberPointRepository memberPointRepository;

    public ProfileResponse get(Long memberId) {
        return memberProfileRepository.getMyProfile(memberId);
    }

    public ProfileResponse update(UpdateProfileRequest updateProfileRequest, Long memberId) {
        ProfileImage profileImage = profileImageRepository.findById(memberId).orElseGet(() -> profileImageRepository.save(ProfileImage.of(memberId, updateProfileRequest.getImageName(), null)));
        Member member = memberUpdater.changeNickname(memberId, updateProfileRequest.getNickname());
        MemberPoint memberPoint = memberPointRepository.getById(memberId);
        return new ProfileResponse(profileImage.getName(), profileImage.getImageUrl(), member.getMemberPrivateInformation().getNickname(), memberPoint.getPoint());
    }

    // 소셜 로그인 성공 ProfileImage(x) or ProfileImage(id, name=null, imageUrl="http..." )
        // 업데이트 -> name값을 업데이트 해주기 -> ProfileImage(id, name="imageName", imageUrl="http..." or null)
    // 일반로그인 : ProfileImage(x) or ProfileImage(id, name="imageName", imageUrl=null)
        // 업데이트 -> ProfileImage(id, name="imageName", imageUrl=null)

    // 프로필 조회 : 존재한다면 name을 우선순위로 리턴해주면된다.

}
