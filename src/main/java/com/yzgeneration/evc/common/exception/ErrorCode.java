package com.yzgeneration.evc.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //400 Bad Request

    //401 Unauthorized

    //403 Forbidden

    //404 NotFound
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "041", "해당 회원이 존재하지 않습니다."),
    USEDITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "042", "해당 중고상품이 존재하지 않습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "043", "해당 이미지가 존재하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String msg;
}
