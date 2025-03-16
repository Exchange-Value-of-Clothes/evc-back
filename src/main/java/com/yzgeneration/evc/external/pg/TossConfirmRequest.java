package com.yzgeneration.evc.external.pg;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TossConfirmRequest {
    private String orderId;
    private String paymentKey;
    private int confirm;
}
