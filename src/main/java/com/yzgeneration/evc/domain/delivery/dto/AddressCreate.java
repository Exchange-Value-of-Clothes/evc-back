package com.yzgeneration.evc.domain.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressCreate {
    @NotBlank
    private String basicAddress;
    private String detailAddress;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
