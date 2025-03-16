package com.yzgeneration.evc.domain.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointChargeOrderResponse {
    private String orderId;
    private int amount;
}
