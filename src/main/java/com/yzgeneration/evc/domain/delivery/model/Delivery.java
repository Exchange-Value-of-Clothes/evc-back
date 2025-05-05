package com.yzgeneration.evc.domain.delivery.model;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Delivery {

    private String orderId;
    private Long senderId;
    private Long receiverId;
    private DeliveryStatus deliveryStatus;
    private ItemType itemType;
    private Long itemId;

    public static Delivery create(Long senderId, Long receiverId, ItemType itemType, Long itemId) {
        return Delivery.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .deliveryStatus(DeliveryStatus.ORDERED)
                .itemType(itemType)
                .itemId(itemId)
                .build();
    }
}
