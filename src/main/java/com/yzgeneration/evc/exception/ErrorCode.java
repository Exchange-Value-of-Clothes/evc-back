package com.yzgeneration.evc.exception;

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
    INVALID_CSRF(HttpStatus.BAD_REQUEST, "004", "잘못된 csrf 토큰 값입니다."),
    SOCIAL_LOGIN(HttpStatus.BAD_REQUEST, "005", "소셜로그인에서 제공하지 않는 플랫폼 로그인 입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "006", "잘못된 요청"),
    SELF_CHAT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "007", "물품 소유자 본인은 본인에게 채팅룸을 개설할 수 없습니다."),
    JsonSerializationException(HttpStatus.BAD_REQUEST, "008", "Json 직렬화 예외"),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "009", "잘못된 닉네임 형식입니다."),
    INVALID_PROVIDER_TYPE(HttpStatus.BAD_REQUEST, "010", "소셜 로그인은 비밀번호를 변경할 수 없습니다."),

    //401 Unauthorized
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "011", "이메일 혹은 비밀번호가 다릅니다."),
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "012-01", "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "012-02", "토큰이 만료되었습니다."),

    //403 Forbidden
    INACTIVE_MEMBER(HttpStatus.FORBIDDEN, "031", "비활성화 된 계정입니다."),

    //404 NotFound
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "041", "해당 회원이 존재하지 않습니다."),
    USEDITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "042", "해당 중고상품이 존재하지 않습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "043", "해당 이미지가 존재하지 않습니다."),
    EMAIL_VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "044", "해당 이메일 인증 내역이 존재하지 않습니다."),
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "045", "해당 채팅이 존재하지 않습니다."),
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "046", "해당 orderId의 결제 정보가 존재하지 않습니다."),
    CHAT_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "047", "해당 채팅방에 속하는 채팅 멤버가 존재하지 않습니다."),
    AUCTIONITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "047", "해당 경매상품은 존재하지 않습니다."),

    // 409 Conflict
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "091", "중복된 이메일이 존재합니다."),
    INVALID_POINT(HttpStatus.CONFLICT, "092", "해당 orderId의 amount가 기존 값과 다릅니다."),


    // 500 Internal Server Error,
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "internal server error."),

    // 503 SERVICE_UNAVAILABLE
    SMTP_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "503", "SMTP server error.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String defaultMessage;
}
