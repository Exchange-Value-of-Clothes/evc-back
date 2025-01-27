package com.yzgeneration.evc.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse {

    private boolean success;

    public static CommonResponse success() {
        return new CommonResponse(true);
    }
}
