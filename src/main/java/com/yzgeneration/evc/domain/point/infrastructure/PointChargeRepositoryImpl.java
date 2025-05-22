package com.yzgeneration.evc.domain.point.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.point.enums.PointChargeStatus;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.yzgeneration.evc.domain.point.infrastructure.QPointChargeEntity.pointChargeEntity;

@Repository
@RequiredArgsConstructor
public class PointChargeRepositoryImpl implements PointChargeRepository {

    private final PointChargeJpaRepository pointChargeJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public PointCharge save(PointCharge pointCharge) {
        return pointChargeJpaRepository.save(PointChargeEntity.from(pointCharge)).toModel();
    }

    @Override
    public PointCharge getById(String orderId) {
        return pointChargeJpaRepository.findById(orderId).orElseThrow(()-> new CustomException(ErrorCode.POINT_NOT_FOUND)).toModel();
    }

    @Override
    public void charge(String orderId) {
        queryFactory.update(pointChargeEntity)
                .set(pointChargeEntity.pointChargeStatus, PointChargeStatus.CHARGED)
                .where(pointChargeEntity.orderId.eq(orderId))
                .execute();
    }

    @Override
    public void cancel(String orderId) {

    }
}
