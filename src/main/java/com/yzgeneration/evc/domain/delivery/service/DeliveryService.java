package com.yzgeneration.evc.domain.delivery.service;

import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreateRequest;
import com.yzgeneration.evc.domain.delivery.impl.DeliveryProcessor;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.external.delivery.Mobility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryProcessor deliveryProcessor;
    private final Mobility mobility;

    public void createOrder(DeliveryCreateRequest request, Long senderId) {
        Delivery delivery = deliveryProcessor.create(request);
        mobility.createQuickOrder(request, delivery.getOrderId());
    }
}
