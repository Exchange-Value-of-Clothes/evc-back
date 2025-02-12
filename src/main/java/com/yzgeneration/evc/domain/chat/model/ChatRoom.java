package com.yzgeneration.evc.domain.chat.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoom {

    private Long id;
    private String usedItemName;
    private Long ownerId;
    private Long senderId;
    private LocalDateTime createdAt;

    public static ChatRoom create(Long ownerId, Long senderId) {
        return ChatRoom.builder()
                .usedItemName("테스트")
                .ownerId(ownerId)
                .senderId(senderId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
