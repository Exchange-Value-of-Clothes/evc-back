package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreate;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.domain.image.enums.ItemType;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
    DeliveryCreate createInfo(String orderId, ItemType itemType, Long itemId);
    Delivery get(String orderId);
}
