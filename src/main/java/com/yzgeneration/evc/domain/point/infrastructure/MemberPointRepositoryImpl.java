package com.yzgeneration.evc.domain.point.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.point.model.MemberPoint;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.yzgeneration.evc.domain.point.infrastructure.QMemberPointEntity.memberPointEntity;

@Repository
@RequiredArgsConstructor
public class MemberPointRepositoryImpl implements MemberPointRepository {

    private final MemberPointJpaRepository jpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public MemberPoint save(MemberPoint memberPoint) {
        return jpaRepository.save(MemberPointEntity.from(memberPoint)).toModel();
    }

    @Override
    public void charge(Long memberId, int point) {
        jpaQueryFactory.update(memberPointEntity)
                .set(memberPointEntity.point, memberPointEntity.point.add(point))
                .where(memberPointEntity.memberId.eq(memberId))
                .execute();
    }

    @Override
    public MemberPoint getById(Long memberId) {
        return jpaRepository.findById(memberId).orElseThrow(() ->new CustomException(ErrorCode.POINT_NOT_FOUND, "멤버의 포인트가 존재하지 않습니다.")).toModel();
    }

}
