package com.yzgeneration.evc.exception;

public class ExternalApiExceptionV2 extends RuntimeException {
    private final int status;
    private final String body;

    public ExternalApiExceptionV2(int status, String body) {
        super("외부 API 오류: " + status);
        this.status = status;
        this.body = body;
    }

    public int getStatus() { return status; }
    public String getBody() { return body; }
}
