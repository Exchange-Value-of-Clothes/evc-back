package com.yzgeneration.evc.external.delivery;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoMobilityOrderResponse {
    private String requestId;
    private String partnerOrderId;
    private Receipt receipt;

    @Getter
    @NoArgsConstructor
    public static class Receipt {
        private String orderId;
        private String orderType;
        private PriceInfo priceInfo;
        private String status;
    }

    @Getter
    @NoArgsConstructor
    public static class PriceInfo {
        private int totalPrice;
        private int cancelFee;
    }
}
