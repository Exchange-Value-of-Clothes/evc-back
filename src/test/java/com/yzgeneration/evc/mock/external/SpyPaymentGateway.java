package com.yzgeneration.evc.mock.external;

import com.yzgeneration.evc.external.pg.Payment;
import com.yzgeneration.evc.external.pg.PaymentGateway;
import com.yzgeneration.evc.fixture.PaymentFixture;

public class SpyPaymentGateway implements PaymentGateway {
    @Override
    public Payment confirm(String orderId, String paymentKey, int amount) {
        return PaymentFixture.of(orderId, paymentKey, amount);
    }

    @Override
    public void confirmWithWebhook(String orderId, String paymentKey, int amount) {

    }
}
