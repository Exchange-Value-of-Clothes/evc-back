package com.yzgeneration.evc.fixture;

import com.yzgeneration.evc.external.pg.Payment;

public final class PaymentFixture extends Fixture{

    private PaymentFixture() {}

    public static Payment of(String orderId, String paymentKey, int amount) {
        return fixtureMonkey.giveMeBuilder(Payment.class)
                .set("totalAmount", amount)
                .set("orderId", orderId)
                .set("paymentKey", paymentKey)
                .sample();
    }
}
