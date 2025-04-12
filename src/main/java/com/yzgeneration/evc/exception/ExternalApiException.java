package com.yzgeneration.evc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExternalApiException extends RuntimeException {
    private String where;
    private String message;
    private int code;

}
