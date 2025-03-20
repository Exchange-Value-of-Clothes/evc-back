package com.yzgeneration.evc.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final String code;
    private final String msg;

    public static ResponseEntity<ErrorResponse> toResponse(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .msg(message)
                        .build());
    }

    public static ErrorResponse from(ErrorCode errorCode){
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .msg(errorCode.getDefaultMessage()).build();
    }

    public static ResponseEntity<String> of(int httpStatus, String msg) {
        return ResponseEntity
                .status(httpStatus)
                .body(msg);
    }
}
