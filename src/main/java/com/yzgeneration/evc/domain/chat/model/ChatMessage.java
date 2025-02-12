package com.yzgeneration.evc.domain.chat.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessage {
    private Long chatRoomId;
    private Long senderId;
    private String content;
    private boolean read;
    private LocalDateTime createdAt;

    public static ChatMessage create(Long chatRoomId, Long senderId, String content, boolean read) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .content(content)
                .read(read)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
