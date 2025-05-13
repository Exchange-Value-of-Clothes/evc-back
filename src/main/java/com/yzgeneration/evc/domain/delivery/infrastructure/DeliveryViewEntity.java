package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.model.DeliveryView;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "delivery_views")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryViewEntity {

    @Id
    private String orderId;
    private String title;
    private String imageName;
    private Long memberId;
    private LocalDateTime createdAt;

    @Builder
    private DeliveryViewEntity(String orderId, String title, String imageName, Long memberId, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.title = title;
        this.imageName = imageName;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public static DeliveryViewEntity from(DeliveryView deliveryView) {
        return DeliveryViewEntity.builder()
                .orderId(deliveryView.getOrderId())
                .title(deliveryView.getTitle())
                .imageName(deliveryView.getImageName())
                .memberId(deliveryView.getMemberId())
                .createdAt(deliveryView.getCreatedAt())
                .build();
    }

    public DeliveryView toModel() {
        return DeliveryView.builder()
                .orderId(orderId)
                .title(title)
                .imageName(imageName)
                .memberId(memberId)
                .createdAt(createdAt)
                .build();
    }

}
