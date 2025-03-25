package com.yzgeneration.evc.domain.member.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity.*;
import static com.yzgeneration.evc.domain.point.infrastructure.QMemberPointEntity.*;

@Repository
@RequiredArgsConstructor
public class MemberProfileRepositoryImpl implements MemberProfileRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public ProfileResponse get(Long memberId) {
//        return queryFactory.select(Projections.constructor(ProfileResponse.class),
//                        memberEntity.memberPrivateInformationEntity.nickname,
//                        )
//                .from(memberEntity)
//                .where(memberEntity.id.eq(memberId))
//                .leftJoin(memberPointEntity)
//                .on(memberEntity.id.eq(memberPointEntity.memberId))
//                .leftJoin()
//                .fetchFirst();
        return null;
    }
}
