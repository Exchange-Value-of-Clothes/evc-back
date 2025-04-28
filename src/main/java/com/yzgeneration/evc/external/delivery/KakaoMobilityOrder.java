package com.yzgeneration.evc.external.delivery;

import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
public class KakaoMobilityOrder {

    private String partnerOrderId; // 연동사 주문 아이디, 유니크
    private String orderType; // QUICK, QUICK_ECONOMY, QUICK_EXPRESS, DOBO
    private ProductInfo productInfo;
    private Pickup pickup;
    private DropOff dropoff;

    @Builder
    private KakaoMobilityOrder(String partnerOrderId, String orderType, ProductInfo productInfo, Pickup pickup, DropOff dropOff) {
        this.partnerOrderId = partnerOrderId;
        this.orderType = orderType;
        this.productInfo = productInfo;
        this.pickup = pickup;
        this.dropoff = dropOff;
    }

    public static KakaoMobilityOrder from(DeliveryCreate request, String orderId) {
        return KakaoMobilityOrder.builder()
                .partnerOrderId(orderId)
                .orderType("QUICK")
                .productInfo(new ProductInfo(request.getPrice(), request.getItemName()))
                .pickup(new Pickup(request.getSenderAddress(), request.getSenderAddressDetail(), request.getSenderLatitude(), request.getReceiverLongitude(), request.getSenderName(), request.getSenderPhone()))
                .dropOff(new DropOff(request.getReceiverAddress(), request.getReceiverAddressDetail(), request.getReceiverLatitude(), request.getReceiverLongitude(), request.getReceiverName(), request.getReceiverPhone()))
                .build();
    }

    @ToString
    @Getter
    @NoArgsConstructor
    private static class ProductInfo {
        private int trayCount; // 포장 묶음 개수 기본 1개, op
        private int totalPrice; // op
        private String size; // xs, s, m, l
        private List<Product> products = new ArrayList<>(); // op

        private ProductInfo(int price, String name) {
            this.trayCount = 1;
            this.totalPrice = price;
            this.size = "M";
            this.products.add(new Product(price, name));
        }

    }

    @ToString
    @Getter
    @NoArgsConstructor
    private static class Product {
        private String name;
        private String quantity;
        private int price;
        private String detail;

        public Product(int price, String name) {
            this.price = price;
            this.quantity = "1";
            this.name = name;
            this.detail = "M";
        }
    }

    @ToString
    @Getter
    @NoArgsConstructor
    public static class Pickup { // 출발지
        private Location location;
        private String wishTime; // op, 퀵,퀵 급송, 대형 : 현재 시간 +20분 ~ +14일, 퀵 이코노미 : 현재 시간 +60분 부터 +14일까지
        private Contact contact;
        private String note; // op, 출발지 메모

        private Pickup(String basicAddress, String detailAddress, Double latitude, Double longitude,
                       String name, String phone) {
            this.location = new Location(basicAddress, detailAddress, latitude, longitude);
            this.contact = new Contact(name, phone);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            this.wishTime = OffsetDateTime.now().plusHours(1).format(formatter); // 밀리초 포함
            this.note = "배송 시 호출 부탁드립니다.";
        }

    }

    @ToString
    @Getter
    @NoArgsConstructor
    public static class DropOff { // 목적지
        private Location location;
        private Contact contact;
        private String note;

        private DropOff(String basicAddress, String detailAddress, Double latitude, Double longitude,
                        String name, String phone) {
            this.location = new Location(basicAddress, detailAddress, latitude, longitude);
            this.contact = new Contact(name, phone);
            this.note = "경비실에 맡겨주세요.";
        }

    }

    @ToString
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

    @ToString
    @Getter
    @NoArgsConstructor
    private static class Contact {
        private String name;
        private String phone;

        private Contact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

    }

}
