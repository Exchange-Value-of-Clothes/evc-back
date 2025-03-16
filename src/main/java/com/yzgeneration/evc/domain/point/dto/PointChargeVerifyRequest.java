package com.yzgeneration.evc.domain.point.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointChargeVerifyRequest {
    private String orderId;
    private int amount;

    @JsonCreator
    public PointChargeVerifyRequest(@JsonProperty("orderId") String orderId, @JsonProperty("amount") int amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
