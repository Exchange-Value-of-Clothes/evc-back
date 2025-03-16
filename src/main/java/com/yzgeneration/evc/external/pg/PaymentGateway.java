package com.yzgeneration.evc.external.pg;

public interface PaymentGateway {
    void confirm(String orderId, String paymentKey, int amount);
}
