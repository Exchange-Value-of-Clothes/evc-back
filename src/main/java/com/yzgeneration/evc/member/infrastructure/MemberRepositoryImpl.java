package com.yzgeneration.evc.member.infrastructure;

import com.yzgeneration.evc.member.service.port.MemberRepository;
import com.yzgeneration.evc.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(MemberEntity.from(member)).toModel();
    }
}
