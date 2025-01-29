package com.yzgeneration.evc.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //400 Bad Request
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "001", "잘못된 비밀번호 형식입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "002", "잘못된 이메일 형식입니다."),
    INVALID_ENUM(HttpStatus.BAD_REQUEST, "003", "잘못된 열거형 형식입니다."),

    //401 Unauthorized
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "011", "이메일 혹은 비밀번호가 다릅니다."),
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "011", "이메일 혹은 비밀번호가 다릅니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "012", "토큰이 만료되었습니다."),

    //403 Forbidden
    INACTIVE_MEMBER(HttpStatus.FORBIDDEN, "031", "비활성화 된 계정입니다."),

    //404 NotFound
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "041", "해당 회원이 존재하지 않습니다."),
    USEDITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "042", "해당 중고상품이 존재하지 않습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "043", "해당 이미지가 존재하지 않습니다."),
    EMAIL_VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "044", "해당 이메일 인증 내역이 존재하지 않습니다."),

    // 409 Conflict
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "091", "중복된 이메일이 존재합니다."),

    // 500 Internal Server Error,
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "internal server error."),
    SMTP_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "501", "SMTP server error.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String defaultMessage;
}
