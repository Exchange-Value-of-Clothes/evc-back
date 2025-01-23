package com.yzgeneration.evc.common.exception;

import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public class ErrorResponse {
    private final String code;
    private final String msg;

    public static ResponseEntity<ErrorResponse> toResponse(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .msg(errorCode.getMsg())
                        .build());
    }
}
