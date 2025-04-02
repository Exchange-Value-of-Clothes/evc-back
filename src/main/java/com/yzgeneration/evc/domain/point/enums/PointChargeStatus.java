package com.yzgeneration.evc.domain.point.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointChargeStatus {
    ORDERED("ORDERED"), CHARGED("CHARGED"), CANCELED("CANCELED");

    private String status;
}
