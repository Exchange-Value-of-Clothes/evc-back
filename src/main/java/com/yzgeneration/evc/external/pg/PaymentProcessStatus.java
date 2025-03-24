package com.yzgeneration.evc.external.pg;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentProcessStatus {
    READY("결제 생성"),
    IN_PROGRESS("결제 인증 완료"),
    WAITING_FOR_DEPOSIT("입금 전(가상계좌)"),
    DONE("결제 승인"),
    CANCELED("승인된 결제 취소"),
    PARTIAL_CANCELED("승인된 결제 부분 취소"),
    ABORTED("결제 승인 실패"),
    EXPIRED("결제 유효 시간 만료 (결제 승인 미요청)");

    private String explain;
}
