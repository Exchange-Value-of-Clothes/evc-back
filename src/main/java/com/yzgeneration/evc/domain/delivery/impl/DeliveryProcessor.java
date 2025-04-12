package com.yzgeneration.evc.domain.delivery.impl;

import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreateRequest;
import com.yzgeneration.evc.domain.delivery.infrastructure.DeliveryRepository;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryProcessor {

    private final DeliveryRepository deliveryRepository;

    public Delivery create(DeliveryCreateRequest request) {
        String itemType = request.getItemType();
        return deliveryRepository.save(null);
    }
}
