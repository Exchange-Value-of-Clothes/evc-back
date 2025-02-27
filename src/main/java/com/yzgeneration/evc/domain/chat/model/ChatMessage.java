package com.yzgeneration.evc.domain.chat.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessage {
    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public static ChatMessage create(Long chatRoomId, Long senderId, String content, Boolean isRead) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .content(content)
                .isRead(isRead)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
