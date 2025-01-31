package com.yzgeneration.evc.domain.member.service.port;

import com.yzgeneration.evc.domain.member.model.Member;

public interface MemberRepository {
    Member save(Member member);
    boolean checkDuplicateEmail(String email);
    Member getByEmail(String email);
    Member getById(Long id);
}
