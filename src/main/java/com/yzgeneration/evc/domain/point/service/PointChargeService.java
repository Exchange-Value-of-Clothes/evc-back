package com.yzgeneration.evc.domain.point.service;

import com.yzgeneration.evc.domain.point.dto.PointChargeConfirmRequest;
import com.yzgeneration.evc.domain.point.dto.PointChargeVerifyRequest;
import com.yzgeneration.evc.domain.point.infrastructure.PointChargeRepository;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointChargeService {

    private final PointChargeRepository pointChargeRepository;

    public PointCharge createOrder(PointCharge pointCharge) {
        return pointChargeRepository.save(pointCharge);
    }

    public void verify(PointChargeVerifyRequest pointChargeVerifyRequest) {
        PointCharge pointCharge = pointChargeRepository.findById(pointChargeVerifyRequest.getOrderId());
        if (pointCharge.getPointChargeType().getPrice() != pointChargeVerifyRequest.getAmount()) throw new CustomException(ErrorCode.INVALID_POINT);
    }

    public void confirm(PointChargeConfirmRequest pointChargeConfirmRequest) {

    }
}
