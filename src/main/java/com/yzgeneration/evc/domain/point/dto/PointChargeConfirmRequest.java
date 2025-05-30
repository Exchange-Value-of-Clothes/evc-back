package com.yzgeneration.evc.domain.point.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointChargeConfirmRequest {
    private String orderId;
    private String paymentKey;
    private int amount;

    @JsonCreator
    public PointChargeConfirmRequest(@JsonProperty("orderId") String orderId,
                                     @JsonProperty("paymentKey") String paymentKey, @JsonProperty("amount") int amount) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }
}
