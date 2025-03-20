package com.yzgeneration.evc.mock.point;

import com.yzgeneration.evc.domain.point.infrastructure.PointChargeRepository;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import com.yzgeneration.evc.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.yzgeneration.evc.exception.ErrorCode.POINT_NOT_FOUND;

public class FakePointChargeRepository implements PointChargeRepository {

    private final List<PointCharge> data = new ArrayList<>();

    @Override
    public PointCharge save(PointCharge pointCharge) {
        data.removeIf(item -> Objects.equals(item.getOrderId(), pointCharge.getOrderId()));
        data.add(pointCharge);
        return pointCharge;
    }

    @Override
    public PointCharge getById(String orderId) {
        return data.stream()
                .filter(item -> item.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new CustomException(POINT_NOT_FOUND));
    }
}
