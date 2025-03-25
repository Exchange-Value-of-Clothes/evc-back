package com.yzgeneration.evc.domain.point.model;

import com.yzgeneration.evc.domain.point.enums.PointChargeStatus;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PointCharge {

    private String orderId;
    private Long memberId;
    private int price;
    private PointChargeStatus pointChargeStatus;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    public static PointCharge create(Long memberId, int price) {
        return PointCharge.builder()
                .memberId(memberId)
                .price(price)
                .pointChargeStatus(PointChargeStatus.ORDERED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void validPoint(int amount) {
        if (this.price != amount) throw new CustomException(ErrorCode.INVALID_POINT);
    }

    public void confirm() {
        this.pointChargeStatus = PointChargeStatus.CHARGED;
    }
}
