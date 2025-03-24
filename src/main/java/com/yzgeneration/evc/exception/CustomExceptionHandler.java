package com.yzgeneration.evc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ErrorResponse.toResponse(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);
        if (e instanceof MethodArgumentTypeMismatchException) {
            String errorMessage = e.getMessage();
            String parameterName = extractParameterName(errorMessage);
            String customErrorMessage = String.format("잘못된 파라미터 요청 '%s'. ", parameterName);
            return ErrorResponse.toResponse(ErrorCode.BAD_REQUEST, customErrorMessage);
        }
        return ErrorResponse.toResponse(ErrorCode.BAD_REQUEST, e.getMessage());
    }

    private String extractParameterName(String errorMessage) {
        Pattern pattern = Pattern.compile("Method parameter '(.*?)'");
        Matcher matcher = pattern.matcher(errorMessage);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown Parameter";
    }
}
