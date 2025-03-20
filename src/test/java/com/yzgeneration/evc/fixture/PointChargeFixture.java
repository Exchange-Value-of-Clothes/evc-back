package com.yzgeneration.evc.fixture;

import com.yzgeneration.evc.domain.point.dto.PointChargeConfirmRequest;
import com.yzgeneration.evc.domain.point.dto.PointChargeOrderRequest;
import com.yzgeneration.evc.domain.point.enums.PointChargeType;
import com.yzgeneration.evc.domain.point.model.PointCharge;

public final class PointChargeFixture extends Fixture{
    private PointChargeFixture() {}

    public static PointCharge createPointCharge(String orderId, Long memberId, PointChargeType pointChargeType) {
        return fixtureMonkey.giveMeBuilder(PointCharge.class)
                .set("orderId", orderId)
                .set("memberId", memberId)
                .set("pointChargeType", pointChargeType)
                .sample();
    }

    public static PointChargeOrderRequest pointChargeOrderRequest() {
        return fixtureMonkey.giveMeBuilder(PointChargeOrderRequest.class)
                .set("pointChargeType", "PACKAGE_5K")
                .sample();
    }

    public static PointChargeConfirmRequest pointChargeConfirmRequest(String orderId, String paymentKey, int amount) {
        return fixtureMonkey.giveMeBuilder(PointChargeConfirmRequest.class)
                .set("orderId", orderId)
                .set("paymentKey", paymentKey)
                .set("amount", amount)
                .sample();
    }
}
