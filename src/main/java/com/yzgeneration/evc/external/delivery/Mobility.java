package com.yzgeneration.evc.external.delivery;


import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreate;

public interface Mobility {
    KakaoMobilityOrderResponse delivery(DeliveryCreate create, String orderId);

    GetKakaoMobilityOrder get(String orderId);
}
