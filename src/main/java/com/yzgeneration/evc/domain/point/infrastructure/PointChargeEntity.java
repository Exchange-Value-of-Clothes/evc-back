package com.yzgeneration.evc.domain.point.infrastructure;

import com.yzgeneration.evc.domain.point.enums.PointChargeStatus;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Entity
@Builder
@Table(name = "point_charges")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PointChargeEntity {

    @Id @Tsid
    private String orderId;
    private Long memberId;
    private int price;
    @Enumerated(EnumType.STRING)
    private PointChargeStatus pointChargeStatus;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    public static PointChargeEntity from(PointCharge pointCharge) {
        return PointChargeEntity.builder()
                .orderId(pointCharge.getOrderId())
                .memberId(pointCharge.getMemberId())
                .price(pointCharge.getPrice())
                .pointChargeStatus(pointCharge.getPointChargeStatus())
                .createdAt(pointCharge.getCreatedAt())
                .paidAt(pointCharge.getPaidAt())
                .build();
    }

    public PointCharge toModel() {
        return PointCharge.builder()
                .orderId(orderId)
                .memberId(memberId)
                .price(price)
                .pointChargeStatus(pointChargeStatus)
                .createdAt(createdAt)
                .paidAt(paidAt)
                .build();
    }

}
