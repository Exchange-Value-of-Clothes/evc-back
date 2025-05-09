package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.model.DeliveryView;

public interface DeliveryViewRepository {
    DeliveryView save(DeliveryView deliveryView);
}
