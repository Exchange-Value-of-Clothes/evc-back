package com.yzgeneration.evc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SocialLoginExceptionHandler {
    @ExceptionHandler(SocialLoginException.class)
    protected ResponseEntity<?> handleSocialLoginException(SocialLoginException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("소셜 로그인 실패", HttpStatus.BAD_REQUEST);
    }
}
