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
    private String imageUrl;
    private Long memberId;
    private LocalDateTime createdAt;

    @Builder
    private DeliveryViewEntity(String orderId, String title, String imageUrl, Long memberId, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public static DeliveryViewEntity from(DeliveryView deliveryView) {
        return DeliveryViewEntity.builder()
                .orderId(deliveryView.getOrderId())
                .title(deliveryView.getTitle())
                .imageUrl(deliveryView.getImageUrl())
                .memberId(deliveryView.getMemberId())
                .createdAt(deliveryView.getCreatedAt())
                .build();
    }

    public DeliveryView toModel() {
        return DeliveryView.builder()
                .orderId(orderId)
                .title(title)
                .imageUrl(imageUrl)
                .memberId(memberId)
                .createdAt(createdAt)
                .build();
    }

}
