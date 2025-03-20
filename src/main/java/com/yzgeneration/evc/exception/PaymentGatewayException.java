package com.yzgeneration.evc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentGatewayException extends RuntimeException {
    private String message;
    private int httpStatus;
}
