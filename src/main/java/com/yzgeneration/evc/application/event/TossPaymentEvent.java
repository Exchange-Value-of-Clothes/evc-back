package com.yzgeneration.evc.application.event;

import com.yzgeneration.evc.external.pg.PaymentStatusChanged;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossPaymentEvent {
    private Long memberId;
    private int price;
    private String status;
    private String orderId;

    public static TossPaymentEvent of(PaymentStatusChanged event, Long memberId) {
        return TossPaymentEvent
                .builder()
                .memberId(memberId)
                .price(event.getData().getTotalAmount())
                .status(event.getData().getStatus())
                .build();
    }
}
