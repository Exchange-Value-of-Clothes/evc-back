package com.yzgeneration.evc.domain.delivery.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DeliveryPayment { // 판매자가 배송비 부담
    private String orderId;
    private Long memberId;
    private Integer amount;
    private Boolean isPaid;
    private LocalDateTime createdAt;
}
