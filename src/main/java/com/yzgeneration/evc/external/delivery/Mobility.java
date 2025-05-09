package com.yzgeneration.evc.external.delivery;


import com.yzgeneration.evc.domain.delivery.dto.MobilityCreate;

public interface Mobility {
    KakaoMobilityOrderResponse delivery(MobilityCreate create);

    GetKakaoMobilityOrder get(String orderId);
}
