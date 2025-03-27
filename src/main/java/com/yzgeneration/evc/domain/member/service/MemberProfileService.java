package com.yzgeneration.evc.domain.member.service;

import com.yzgeneration.evc.domain.image.infrastructure.repository.ProfileImageRepository;
import com.yzgeneration.evc.domain.image.model.ProfileImage;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.dto.UpdateProfileRequest;
import com.yzgeneration.evc.domain.member.infrastructure.MemberProfileRepository;
import com.yzgeneration.evc.domain.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberRepository memberRepository;
    private final ProfileImageRepository profileImageRepository;

    public ProfileResponse get(Long memberId) {
        Optional<ProfileImage> profileImageOptional = profileImageRepository.findById(memberId);
        if(profileImageOptional.isPresent()) {

        }
        return null;
    }

    // 소셜 로그인 성공 ProfileImage(id, name=null, imageUrl="http..." or null)
        // 업데이트 -> name값을 업데이트 해주기 -> ProfileImage(id, name="imageName", imageUrl="http..." or null)
    // 일반로그인 : ProfileImage(x)
        // 업데이트 -> ProfileImage(id, name="imageName", imageUrl=null)

    // 프로필 조회 : 존재한다면 name을 우선순위로 리턴해주면된다.

}
