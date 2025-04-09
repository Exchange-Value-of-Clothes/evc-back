package com.yzgeneration.evc.domain.delivery;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Delivery {

    private String orderId;
    private Long senderId;
    private Long receiverId;
    private DeliveryStatus deliveryStatus;
    private Address pickupAddress;
    private Address dropOffAddress;
}
