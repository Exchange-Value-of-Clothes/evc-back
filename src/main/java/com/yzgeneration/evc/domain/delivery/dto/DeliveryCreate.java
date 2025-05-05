package com.yzgeneration.evc.domain.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class DeliveryCreate {
    private String orderId;
    private int price;
    private String itemName;
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String senderAddressDetail;
    private double senderLatitude;
    private double senderLongitude;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String receiverAddressDetail;
    private double receiverLatitude;
    private double receiverLongitude;

}
