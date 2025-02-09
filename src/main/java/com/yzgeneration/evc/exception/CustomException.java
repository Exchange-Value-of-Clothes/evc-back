package com.yzgeneration.evc.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private String customMessage;

    public CustomException(ErrorCode errorCode, String customMessage) {
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return customMessage != null ? customMessage : errorCode.getDefaultMessage();
    }
}
