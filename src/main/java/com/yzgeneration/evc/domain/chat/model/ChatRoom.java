package com.yzgeneration.evc.domain.chat.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoom {

    private Long id;
    private Long usedItemId;
    private Long ownerId;
    private Long participantId;
    private LocalDateTime createdAt;

    public static ChatRoom create(Long usedItemId, Long ownerId, Long participantId) {
        return ChatRoom.builder()
                .usedItemId(usedItemId)
                .ownerId(ownerId)
                .participantId(participantId)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
