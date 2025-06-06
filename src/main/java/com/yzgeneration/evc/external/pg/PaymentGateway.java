package com.yzgeneration.evc.external.pg;

public interface PaymentGateway {
    Payment confirm(String orderId, String paymentKey, int amount);
    void confirmWithWebhook(String orderId, String paymentKey, int amount);
}
