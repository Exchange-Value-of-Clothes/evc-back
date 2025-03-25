package com.yzgeneration.evc.domain.point.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointChargeOrderRequest{

    private int price;

    @JsonCreator
    public PointChargeOrderRequest(@JsonProperty("price") int price) {
        this.price = price;
    }

}
