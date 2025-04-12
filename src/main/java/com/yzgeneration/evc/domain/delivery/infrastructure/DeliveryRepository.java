package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.model.Delivery;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
}
