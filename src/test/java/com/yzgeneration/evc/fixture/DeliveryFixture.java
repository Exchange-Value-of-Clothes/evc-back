package com.yzgeneration.evc.fixture;

import com.yzgeneration.evc.domain.delivery.dto.*;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.external.delivery.GetKakaoMobilityOrder;
import com.yzgeneration.evc.external.delivery.KakaoMobilityOrderResponse;


import java.util.ArrayList;
import java.util.List;

public class DeliveryFixture extends Fixture {
    protected DeliveryFixture() {}

    private static List<Datum> data = new ArrayList<>();

    private static class Datum {
        private String address_name;
        private String x;
        private String y;

        private Datum(String address_name, String x, String y) {
            this.address_name = address_name;
            this.x = x;
            this.y = y;
        }
    }

    public static SearchCoordinateResponse createSearchCoordinateResponse() {
        data.add(new Datum("address_name", "x", "y"));
        return fixtureMonkey.giveMeBuilder(SearchCoordinateResponse.class)
                .set("data", data)
                .set("$.data[0].address_name", data.get(0).address_name)
                .set("$.data[0].x", data.get(0).x)
                .set("$.data[0].y", data.get(0).y)
                .sample();
    }

    public static SearchCoordinate createSearchCoordinate(String addressName) {
        return fixtureMonkey.giveMeBuilder(SearchCoordinate.class)
                .set("addressName", addressName)
                .sample();
    }

    public static AddressCreate createAddressCreate(String basicAddress, String detailAddress, Double latitude, Double longitude) {
        return fixtureMonkey.giveMeBuilder(AddressCreate.class)
                .set("basicAddress", basicAddress)
                .set("detailAddress", detailAddress)
                .set("latitude", latitude)
                .set("longitude", longitude)
                .sample();
    }

    public static MobilityCreate createDeliveryCreate(Delivery delivery) {
        return fixtureMonkey.giveMeBuilder(MobilityCreate.class)
                .set("orderId", delivery.getOrderId())
                .set("price", 1000)
                .set("itemName", "itemName")
                .set("senderName", "senderName")
                .set("senderPhone", "01012345678")
                .set("senderAddress", "senderAddress")
                .set("senderAddressDetail", "senderAddressDetail")
                .set("senderLatitude", 0.0)
                .set("senderLongitude", 0.0)
                .set("receiverName", "receiverName")
                .set("receiverPhone", "01012345678")
                .set("receiverAddress", "receiverAddress")
                .set("receiverAddressDetail", "receiverAddressDetail")
                .set("receiverLatitude", 0.0)
                .set("receiverLongitude", 0.0)
                .sample();

    }

    public static KakaoMobilityOrderResponse createKakaoMobilityOrderResponse(String orderId) {
        return fixtureMonkey.giveMeBuilder(KakaoMobilityOrderResponse.class)
                .set("requestId", "requestId")
                .set("partnerOrderId", "partnerOrderId")
                .set("receipt.orderId", orderId)
                .set("receipt.orderType", "QUICK")
                .set("receipt.priceInfo.totalPrice", 1000)
                .set("receipt.priceInfo.cancelFee", 0)
                .set("receipt.status", "COMPLETED")
                .sample();

    }

    public static GetKakaoMobilityOrder createGetKakaoMobilityOrder() {
        return fixtureMonkey.giveMeBuilder(GetKakaoMobilityOrder.class)
                .set("requestId", "requestId")
                .set("partnerOrderId", "partnerOrderId")
                .set("pickup.location.basicAddress", "인천광역시 부평북로 431")
                .set("pickup.location.detailAddress", "주공 미래타운 1단지")
                .set("pickup.location.latitude", 0.0)
                .set("pickup.location.longitude", 0.0)
                .set("pickup.wishTime", "2025-04-24T20:29:26.184+09:00")
                .set("pickup.contact.name", "김보내")
                .set("pickup.contact.phone", "01012345678")
                .set("pickup.note", "잘 보내주세요")
                .set("dropoff.location.basicAddress", "인천광역시 부평북로 432")
                .set("dropoff.location.detailAddress", "주공 미래타운 2단지")
                .set("dropoff.location.latitude", 0.0)
                .set("dropoff.location.longitude", 0.0)
                .set("dropoff.contact.name", "김받아")
                .set("dropoff.contact.phone", "01033337778")
                .set("dropoff.note", "문 앞에 놓아주세요")
                .set("receipt.orderId", "OS782T")
                .set("receipt.orderType", "QUICK")
                .set("receipt.priceInfo.totalPrice", 1000)
                .set("receipt.priceInfo.cancelFee", 0)
                .set("receipt.status", "MATCHING_FAILED")
                .set("receipt.histories", List.of(
                                createHistory("MATCHING_FAILED", "2025-04-24T20:29:26.184+09:00")
                        ))
                .sample();
    }

    public static DeliveryPrepare createDeliveryPrepare() {
        return fixtureMonkey.giveMeBuilder(DeliveryPrepare.class)
                .set("itemType", "AUCTIONITEM")
                .set("itemId", 1L)
                .set("buyerId", 1L)
                .set("sellerId", 2L)
                .sample();
    }

    private static GetKakaoMobilityOrder.History createHistory(String status, String updatedAt) {
        GetKakaoMobilityOrder.History history = new GetKakaoMobilityOrder.History();
        history.setStatus(status);
        history.setUpdatedAt(updatedAt);
        return history;
    }
}
