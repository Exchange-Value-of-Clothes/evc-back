package com.yzgeneration.evc.domain.delivery.model;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    ORDERED, MATCHING, CANCEL, PICKUP, DELIVERED, RESERVED;
}
