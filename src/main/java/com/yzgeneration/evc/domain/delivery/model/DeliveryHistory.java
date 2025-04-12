package com.yzgeneration.evc.domain.delivery.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryHistory {

    private String orderId;
    private Long memberId;
}
