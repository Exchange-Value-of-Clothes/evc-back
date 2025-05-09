package com.yzgeneration.evc.domain.delivery.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DeliveryView {
    private String orderId;
    private String title;
    private String imageUrl;
    private Long memberId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public static DeliveryView of(String orderId, String title, String imageUrl, Long memberId) {
        return DeliveryView.builder()
                .orderId(orderId)
                .title(title)
                .imageUrl(imageUrl)
                .memberId(memberId)
                .createdAt(LocalDateTime.now())
                .build();

    }
}
