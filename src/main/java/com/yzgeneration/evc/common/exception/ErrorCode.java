package com.yzgeneration.evc.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //400 Bad Request
    PASSWORD_INCORRECT_FORMAT(HttpStatus.BAD_REQUEST, "001", "잘못된 비밀번호 형식입니다."),
    EMAIL_INCORRECT_FORMAT(HttpStatus.BAD_REQUEST, "002", "잘못된 이메일 형식입니다."),

    //401 Unauthorized

    //403 Forbidden

    //404 NotFound
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "041", "해당 회원이 존재하지 않습니다."),
    USEDITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "042", "해당 중고상품이 존재하지 않습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "043", "해당 이미지가 존재하지 않습니다."),
    EMAIL_VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "044", "해당 이메일 인증 내역이 존재하지 않습니다."),

    // 409 Conflict
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "091", "중복된 이메일이 존재합니다."),

    // 500 Internal Server Error
    SMTP_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "SMTP server error."),;
    private final HttpStatus httpStatus;
    private final String code;
    private final String msg;
}
