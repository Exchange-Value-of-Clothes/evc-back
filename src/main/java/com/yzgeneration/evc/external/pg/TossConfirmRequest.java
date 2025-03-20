package com.yzgeneration.evc.external.pg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TossConfirmRequest {
    private String orderId;
    private String paymentKey;
    private int amount;
}
