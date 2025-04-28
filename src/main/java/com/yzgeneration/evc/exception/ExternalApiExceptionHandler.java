package com.yzgeneration.evc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice
public class ExternalApiExceptionHandler {

    @ExceptionHandler(SocialLoginException.class)
    protected ResponseEntity<?> handleSocialLoginException(SocialLoginException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("소셜 로그인 실패", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalApiException.class)
    protected ResponseEntity<?> handleExternalException(ExternalApiException e) {
        return ErrorResponse.of(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ExternalApiExceptionV2.class)
    protected ResponseEntity<?> handleExternalApiException(ExternalApiExceptionV2 ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ex.getBody());
    }


}
