package com.yzgeneration.evc.domain.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Chatting  {
    private Long memberId;
    private String msg;
    private LocalDateTime createdAt;

}
