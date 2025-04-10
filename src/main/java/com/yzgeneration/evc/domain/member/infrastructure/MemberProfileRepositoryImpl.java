package com.yzgeneration.evc.domain.member.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.yzgeneration.evc.domain.image.infrastructure.entity.QProfileImageEntity.profileImageEntity;
import static com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity.*;
import static com.yzgeneration.evc.domain.point.infrastructure.QMemberPointEntity.*;

@Repository
@RequiredArgsConstructor
public class MemberProfileRepositoryImpl implements MemberProfileRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ProfileResponse getMyProfile(Long memberId) {
        return queryFactory.select(Projections.constructor(ProfileResponse.class,
                        profileImageEntity.name,
                        profileImageEntity.imageUrl, // locate(substring, string)
                        Expressions.stringTemplate("substring({0}, 1, locate('#', {0}) - 1)", // string, start_position(default=1), length(가져올 문자 수)
                                memberEntity.memberPrivateInformationEntity.nickname),
                        memberPointEntity.point,
                        profileImageEntity.isSocialProfileVisible
                        ))
                .from(memberEntity)
                .where(memberEntity.id.eq(memberId))
                .leftJoin(memberPointEntity).on(memberEntity.id.eq(memberPointEntity.memberId))
                .leftJoin(profileImageEntity).on(memberEntity.id.eq(profileImageEntity.memberId))
                .fetchFirst();

    }
}
