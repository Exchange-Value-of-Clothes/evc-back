package com.yzgeneration.evc.domain.point.implement;

import com.yzgeneration.evc.domain.point.infrastructure.PointChargeRepository;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointChargeValidator {
    private final PointChargeRepository pointChargeRepository;

    public void validate(String orderId, int amount) {
        PointCharge pointCharge = pointChargeRepository.getById(orderId);
        pointCharge.validPoint(amount);
    }
}
