package com.yzgeneration.evc.domain.point.infrastructure;

import com.yzgeneration.evc.domain.point.model.PointCharge;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointChargeRepositoryImpl implements PointChargeRepository {

    private final PointChargeJpaRepository pointChargeJpaRepository;

    @Override
    public PointCharge save(PointCharge pointCharge) {
        return pointChargeJpaRepository.save(PointChargeEntity.from(pointCharge)).toModel();
    }

    @Override
    public PointCharge findById(String orderId) {
        return pointChargeJpaRepository.findById(orderId).orElseThrow(()-> new CustomException(ErrorCode.POINT_NOT_FOUND)).toModel();
    }
}
