package com.yzgeneration.evc.domain.member.infrastructure;

import com.yzgeneration.evc.domain.member.dto.MemberPrivateInfoResponse;
import com.yzgeneration.evc.domain.member.model.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    boolean checkDuplicateEmail(String email);
    Member getByEmail(String email);
    Member getById(Long id);
    Optional<Member> findSocialMember(String providerType, String providerId);
    MemberPrivateInfoResponse getPrivateInfo(Long memberId);
}
