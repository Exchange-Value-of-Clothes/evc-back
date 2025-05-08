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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryProcessor deliveryProcessor;
    private final Mobility mobility;

    public KakaoMobilityOrderResponse order(ItemType itemType, Long itemId, Long buyerId, Long sellerId, Long memberId) {
        if (!sellerId.equals(memberId)) throw new CustomException(ErrorCode.DELIVERY_ONLY_SELLER_REQUEST);
        Delivery delivery = deliveryProcessor.create(itemType, itemId, buyerId, sellerId);
        DeliveryCreate deliveryCreate = deliveryProcessor.prepare(delivery.getOrderId(), itemType, itemId);
        log.info(deliveryCreate.toString());
        KakaoMobilityOrderResponse response = mobility.delivery(deliveryCreate, delivery.getOrderId());
        // todo 메시지 큐 기반으로 재처리 시도
        if (!response.getReceipt().getStatus().isBlank()) deliveryProcessor.order(delivery);
        return response;
    }

    public GetKakaoMobilityOrder get(String orderId, Long memberId) {
        deliveryProcessor.get(orderId, memberId);
        return mobility.get(orderId);
    }
}
