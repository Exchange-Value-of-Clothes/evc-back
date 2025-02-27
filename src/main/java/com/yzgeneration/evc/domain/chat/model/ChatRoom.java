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
    private Long participationId;
    private LocalDateTime createdAt;

    public static ChatRoom create(Long usedItemId, Long ownerId) {
        return ChatRoom.builder()
                .usedItemId(usedItemId)
                .ownerId(ownerId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void enter(Long memberId) {
        this.participationId = memberId;
    }
}
