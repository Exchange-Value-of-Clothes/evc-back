package com.yzgeneration.evc.domain.devicetoken.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeviceToken {
    private Long id;
    private Long memberId;
    private String deviceToken;
    private boolean active;
    private LocalDateTime createdAt;

}
