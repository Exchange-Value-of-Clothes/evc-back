package com.yzgeneration.evc.domain.delivery;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryHistory {

    private String orderId;
    private Long memberId;
}
