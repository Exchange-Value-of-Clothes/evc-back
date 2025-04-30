package com.yzgeneration.evc.domain.chat.model;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoom {

    private Long id;
    private Long itemId;
    private Long ownerId;
    private Long participantId;
    private ItemType itemType;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public static ChatRoom create(Long itemId, Long ownerId, Long participantId, ItemType itemType) {
        return ChatRoom.builder()
                .itemId(itemId)
                .ownerId(ownerId)
                .participantId(participantId)
                .itemType(itemType)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
