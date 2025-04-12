package com.yzgeneration.evc.external;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoKapiErrorResponse {
    private int code;
    private String msg;
}
