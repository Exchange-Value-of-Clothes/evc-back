package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.model.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private String basicAddress;
    private String detailAddress;
    private double latitude;
    private double longitude;

    @Builder
    private AddressEntity(Long id, Long memberId, String basicAddress, String detailAddress, double latitude, double longitude) {
        this.id = id;
        this.memberId = memberId;
        this.basicAddress = basicAddress;
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static AddressEntity from(Address address) {
        return AddressEntity.builder()
                .id(address.getId())
                .memberId(address.getMemberId())
                .basicAddress(address.getBasicAddress())
                .detailAddress(address.getDetailAddress())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
    }

    public Address toModel() {
        return Address.builder()
                .id(id)
                .memberId(memberId)
                .basicAddress(basicAddress)
                .detailAddress(detailAddress)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
