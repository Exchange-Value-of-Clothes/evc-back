package com.yzgeneration.evc.domain.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressCreate { // todo 위도 경도 범위 제한, 위도 y
    @NotBlank
    private String basicAddress;
    private String detailAddress;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
