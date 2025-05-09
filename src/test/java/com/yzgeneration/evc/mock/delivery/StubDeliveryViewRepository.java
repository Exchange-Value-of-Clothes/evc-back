package com.yzgeneration.evc.mock.delivery;

import com.yzgeneration.evc.domain.delivery.infrastructure.DeliveryViewRepository;
import com.yzgeneration.evc.domain.delivery.model.DeliveryView;

public class StubDeliveryViewRepository implements DeliveryViewRepository {
    @Override
    public DeliveryView save(DeliveryView deliveryView) {
        return null;
    }
}
