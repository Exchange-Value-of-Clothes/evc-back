package com.yzgeneration.evc.mock.delivery;

import com.yzgeneration.evc.domain.delivery.dto.MobilityCreate;
import com.yzgeneration.evc.external.delivery.GetKakaoMobilityOrder;
import com.yzgeneration.evc.external.delivery.KakaoMobilityOrderResponse;
import com.yzgeneration.evc.external.delivery.Mobility;
import com.yzgeneration.evc.fixture.DeliveryFixture;

public class StubMobility implements Mobility {

    @Override
    public KakaoMobilityOrderResponse delivery(MobilityCreate create) {
        return DeliveryFixture.createKakaoMobilityOrderResponse(create.getOrderId());
    }

    @Override
    public GetKakaoMobilityOrder get(String orderId) {
        return DeliveryFixture.createGetKakaoMobilityOrder();
    }
}
