package com.yzgeneration.evc.domain.member.infrastructure;

import com.yzgeneration.evc.domain.member.dto.ProfileResponse;

public interface MemberProfileRepository {
    ProfileResponse get(Long memberId);
}
