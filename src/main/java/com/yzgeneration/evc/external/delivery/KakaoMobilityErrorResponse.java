package com.yzgeneration.evc.external.delivery;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoMobilityErrorResponse {
    private String requestId;
    private int code;
    private int status;
    private String message;
}
