package com.yzgeneration.evc.domain.delivery.service;

import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreate;
import com.yzgeneration.evc.domain.delivery.impl.DeliveryProcessor;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.external.delivery.GetKakaoMobilityOrder;
import com.yzgeneration.evc.external.delivery.KakaoMobilityOrderResponse;
import com.yzgeneration.evc.external.delivery.Mobility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryProcessor deliveryProcessor;
    private final Mobility mobility;

    public KakaoMobilityOrderResponse order(ItemType itemType, Long itemId, Long buyerId, Long sellerId, Long memberId) {
        if (!sellerId.equals(memberId)) throw new CustomException(ErrorCode.DELIVERY_ONLY_SELLER_REQUEST);
        Delivery delivery = deliveryProcessor.create(itemType, itemId, buyerId, sellerId);
        DeliveryCreate deliveryCreate = deliveryProcessor.prepare(delivery.getOrderId(), itemType, itemId);
        return mobility.delivery(deliveryCreate, delivery.getOrderId());
    }

    public GetKakaoMobilityOrder get(String orderId, Long memberId) {
        deliveryProcessor.get(orderId, memberId);
        return mobility.get(orderId);
    }
}
