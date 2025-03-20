package com.yzgeneration.evc.external.pg;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentStatusChanged {
    private String eventType;
    private String createdAt;
    private Payment data;

    @Override
    public String toString() {
        return "PaymentStatusChanged{" +
                "eventType='" + eventType + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", data=" + data +
                '}';
    }
}
