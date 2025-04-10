package com.yzgeneration.evc.mock.member;

import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.infrastructure.MemberProfileRepository;

public class StubMemberProfileRepository implements MemberProfileRepository {
    @Override
    public ProfileResponse getMyProfile(Long memberId) {
        return new ProfileResponse("imageName", "imageUrl", "nickname", 1000, true);
    }
}
