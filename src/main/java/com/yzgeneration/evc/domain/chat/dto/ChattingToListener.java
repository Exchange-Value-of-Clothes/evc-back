package com.yzgeneration.evc.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingToListener {
    private Long chatRoomId;
    private Long memberId;
    private String content;
    private LocalDateTime createdAt;
    private boolean chatPartnerExist;

    public static ChattingToListener of(Long chatRoomId, Long memberId, String content, LocalDateTime createdAt, boolean chatPartnerExist) {
        return new ChattingToListener(chatRoomId, memberId, content, createdAt, chatPartnerExist);
    }
}
