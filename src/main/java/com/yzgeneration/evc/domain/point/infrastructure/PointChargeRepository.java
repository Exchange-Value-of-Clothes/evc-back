package com.yzgeneration.evc.domain.point.infrastructure;

import com.yzgeneration.evc.domain.point.model.PointCharge;

public interface PointChargeRepository {
    PointCharge save(PointCharge pointCharge);
    PointCharge getById(String orderId);
}
