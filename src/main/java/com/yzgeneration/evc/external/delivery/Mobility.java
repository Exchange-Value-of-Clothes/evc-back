package com.yzgeneration.evc.external.delivery;

import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreateRequest;

public interface Mobility {
    void createQuickOrder(DeliveryCreateRequest request, String orderId);
}
