package com.yzgeneration.evc.domain.delivery;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Address {
    private String basicAddress;
    private String detailAddress;
    private Double latitude;
    private Double longitude;
}
