package com.yzgeneration.evc.external.delivery;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@NoArgsConstructor
public class GetKakaoMobilityOrder {
    private String requestId;
    private String partnerOrderId;
    private KakaoMobilityOrder.Pickup pickup;
    private KakaoMobilityOrder.DropOff dropoff;
    private Receipt receipt;

    @Getter
    @NoArgsConstructor
    public static class Receipt {
        private String orderId;
        private String orderType;
        private KakaoMobilityOrderResponse.PriceInfo priceInfo;
        private String status;
        private List<History> histories;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class History {
        private String status;
        private String updatedAt;
    }


}
