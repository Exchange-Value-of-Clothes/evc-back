package com.yzgeneration.evc.domain.chat.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Chatting  {
    private Long memberId;
    private String msg;
    private LocalDateTime createdAt;
}
