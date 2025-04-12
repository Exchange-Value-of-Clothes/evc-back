package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.domain.delivery.model.DeliveryStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "deliveries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryEntity {

    @Id @Tsid
    private String orderId;
    private Long senderId;
    private Long receiverId;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Builder
    private DeliveryEntity(Long senderId, Long receiverId, DeliveryStatus deliveryStatus) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.deliveryStatus = deliveryStatus;
    }

    public static DeliveryEntity from(Delivery delivery) {
        return DeliveryEntity.builder()
                .senderId(delivery.getSenderId())
                .receiverId(delivery.getReceiverId())
                .deliveryStatus(delivery.getDeliveryStatus())
                .build();
    }

    public Delivery toModel() {
        return Delivery.builder()
                .orderId(orderId)
                .senderId(senderId)
                .receiverId(receiverId)
                .deliveryStatus(deliveryStatus)
                .build();
    }

}
