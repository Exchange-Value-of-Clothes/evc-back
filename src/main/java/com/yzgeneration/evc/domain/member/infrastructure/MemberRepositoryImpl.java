package com.yzgeneration.evc.domain.member.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.delivery.infrastructure.QAddressEntity;
import com.yzgeneration.evc.domain.member.dto.MemberPrivateInfoResponse;
import com.yzgeneration.evc.domain.member.enums.ProviderType;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.yzgeneration.evc.domain.delivery.infrastructure.QAddressEntity.addressEntity;
import static com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity.memberEntity;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final JPAQueryFactory queryFactory;

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

    @Override
    public Optional<Member> findSocialMember(String providerType, String providerId) {
        return memberJpaRepository.findByMemberAuthenticationInformationEntityProviderTypeAndMemberAuthenticationInformationEntityProviderId(ProviderType.valueOf(providerType), providerId).flatMap(memberEntity -> Optional.of(memberEntity.toModel()));
    }

    @Override
    public MemberPrivateInfoResponse getPrivateInfo(Long memberId) {
        return queryFactory.select(Projections.constructor(MemberPrivateInfoResponse.class,
                                memberEntity.memberPrivateInformationEntity.accountName,
                                memberEntity.memberPrivateInformationEntity.accountNumber,
                                memberEntity.memberPrivateInformationEntity.phoneNumber,
                                addressEntity.basicAddress,
                                addressEntity.detailAddress,
                                addressEntity.latitude,
                                addressEntity.longitude))
                .from(memberEntity)
                .where(memberEntity.id.eq(memberId))
                .leftJoin(addressEntity).on(memberEntity.id.eq(addressEntity.memberId))
                .fetchFirst();
    }
}
