package com.yzgeneration.evc.domain.delivery.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Delivery {

    private String orderId;
    private Long senderId;
    private Long receiverId;
    private DeliveryStatus deliveryStatus;

    public static Delivery create(Long senderId, Long receiverId) {
        return Delivery.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .deliveryStatus(DeliveryStatus.MATCHING)
                .build();
    }
}
