package com.yzgeneration.evc.domain.member.infrastructure;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import com.yzgeneration.evc.domain.member.model.Member;
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

    @Override
    public boolean checkDuplicateEmail(String email) {
        return memberJpaRepository.existsByMemberPrivateInformationEntityEmail(email);
    }

    @Override
    public Member getByEmail(String email) {
        return memberJpaRepository.findByMemberPrivateInformationEntityEmail(email).orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toModel();
    }

    @Override
    public Member getById(Long id) {
        return memberJpaRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toModel();
    }
}
