package com.yzgeneration.evc.domain.delivery.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Address {
    private Long id;
    private Long memberId;
    private String basicAddress;
    private String detailAddress;
    private double latitude;
    private double longitude;

    public static Address create(Long memberId, String basicAddress, String detailAddress, double latitude, double longitude) {
        return Address.builder()
                .memberId(memberId)
                .basicAddress(basicAddress)
                .detailAddress(detailAddress)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public void update(String basicAddress, String detailAddress, Double latitude, Double longitude) {
        this.basicAddress = basicAddress;
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
