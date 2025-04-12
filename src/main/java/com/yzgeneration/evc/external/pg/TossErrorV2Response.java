package com.yzgeneration.evc.external.pg;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossErrorV2Response {
    private String traceId;
    private String message;
    private Error error;

    @Getter
    @NoArgsConstructor
    public static class Error {
        private String code;
        private String message;
    }
}
