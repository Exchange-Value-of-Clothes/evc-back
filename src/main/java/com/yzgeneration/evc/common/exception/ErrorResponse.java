package com.yzgeneration.evc.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final String code;
    private final String msg;

    public static ResponseEntity<ErrorResponse> toResponse(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .msg(errorCode.getDefaultMessage())
                        .build());
    }

    public static ErrorResponse from(ErrorCode errorCode){
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .msg(errorCode.getDefaultMessage()).build();
    }
}
