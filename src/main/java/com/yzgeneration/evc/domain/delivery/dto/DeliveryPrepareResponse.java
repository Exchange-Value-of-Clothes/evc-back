package com.yzgeneration.evc.domain.delivery.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryPrepareResponse {

    private Buyer buyer;
    private Seller seller;
    private String itemType;
    private Long itemId;
    private String itemName;
    private int price;

    @Getter
    @NoArgsConstructor
    private static class Buyer {
        private String accountName;
        private String accountNumber;
        private String phoneNumber;
        private String address;
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @NoArgsConstructor
    private static class Seller {
        private String accountName;
        private String accountNumber;
        private String phoneNumber;
        private String address;
        private Double latitude;
        private Double longitude;
    }
}
