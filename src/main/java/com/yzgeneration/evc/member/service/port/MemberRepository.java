package com.yzgeneration.evc.member.service.port;

import com.yzgeneration.evc.member.model.Member;

public interface MemberRepository {

    Member save(Member member);
    boolean checkDuplicateEmail(String email);
}
