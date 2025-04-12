package com.yzgeneration.evc.external.delivery;

import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoMobilityOrder {

    private String partnerOrderId; // 연동사 주문 아이디, 유니크
    private String orderType; // QUICK, QUICK_ECONOMY, QUICK_EXPRESS, DOBO
    private ProductInfo productInfo;
    private List<Product> products; // op
    private Pickup pickup;
    private DropOff dropOff;

    @Builder
    public KakaoMobilityOrder(String partnerOrderId, String orderType, ProductInfo productInfo, List<Product> products, Pickup pickup, DropOff dropOff) {
        this.partnerOrderId = partnerOrderId;
        this.orderType = orderType;
        this.productInfo = productInfo;
        this.products = products;
        this.pickup = pickup;
        this.dropOff = dropOff;
    }

    public static KakaoMobilityOrder from(DeliveryCreateRequest request, String orderId) {
        return KakaoMobilityOrder.builder()
                .partnerOrderId(orderId)
                .orderType("QUICK")
//                .productInfo(new ProductInfo(request.getPrice(), request.getSize()))
//                .pickup(new Pickup(request.getPickupLocation(), request.getPickupLocationDetail(), request.getPickupLatitude(), request.getPickupLongitude()))
                .build();
    }

    @Getter
    @NoArgsConstructor
    private static class ProductInfo {
        private int trayCount; // 포장 묶음 개수 기본 1개, op
        private int totalPrice; // op
        private String size; // xs, s, m, l

        private ProductInfo(int price, String size) {
            this.trayCount = 1;
            this.totalPrice = price;
            this.size = "m";
        }

    }

    @Getter
    @NoArgsConstructor
    private static class Product {
        private String name;
        private String quantity;
        private int price;
        private String detail;
    }

    @Getter
    @NoArgsConstructor
    private static class Pickup { // 출발지
        private Location location;
        private OffsetDateTime wishTime; // op, 퀵,퀵 급송, 대형 : 현재 시간 +20분 ~ +14일, 퀵 이코노미 : 현재 시간 +60분 부터 +14일까지
        private Contact contact;
        private String note; // op, 출발지 메모

        private Pickup(String basicAddress, String detailAddress, Double latitude, Double longitude) {
            this.location = new Location(basicAddress, detailAddress, latitude, longitude);
        }
    }

    @Getter
    @NoArgsConstructor
    private static class DropOff { // 목적지
        private Location location;
        private Contact contact;
        private String note;
    }

    @Getter
    @NoArgsConstructor
    private static class Location {
        private String basicAddress;
        private String detailAddress; // op
        private Double latitude;
        private Double longitude;

        private Location(String basicAddress, String detailAddress, Double latitude, Double longitude) {
            this.basicAddress = basicAddress;
            this.detailAddress = detailAddress;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Getter
    @NoArgsConstructor
    private static class Contact {
        private String name;
        private String phone;
    }

}
