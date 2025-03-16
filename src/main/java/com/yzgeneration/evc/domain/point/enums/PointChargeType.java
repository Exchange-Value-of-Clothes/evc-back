package com.yzgeneration.evc.domain.point.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointChargeType {

    PACKAGE_5K(5_000), PACKAGE_10K(10_000), PACKAGE_50K(50_000);

    private int price;
}
