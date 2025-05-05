package com.yzgeneration.evc.domain.delivery.impl;

import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreate;
import com.yzgeneration.evc.domain.delivery.infrastructure.DeliveryRepository;

import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.domain.image.enums.ItemType;


import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryProcessor {

    private final DeliveryRepository deliveryRepository;

    public Delivery create(ItemType itemType, Long itemId, Long buyerId, Long sellerId) {
        // todo : 상대방 아이디 존재 여부 검증, 상품 아이디 존재 검증
        return deliveryRepository.save(Delivery.create(sellerId, buyerId, itemType, itemId));
    }

    public DeliveryCreate prepare(String orderId, ItemType itemType, Long itemId) {
        return deliveryRepository.createInfo(orderId, itemType, itemId);
    }

    public void get(String orderId, Long memberId) {
        Delivery delivery = deliveryRepository.get(orderId);
        if (!delivery.getSenderId().equals(memberId) && !delivery.getReceiverId().equals(memberId)) throw new CustomException(ErrorCode.DELIVERY_NOT_FOUND);
    }
}
