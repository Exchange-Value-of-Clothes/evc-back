package com.yzgeneration.evc.domain.chat.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMember {
    private Long id;
    private Long chatRoomId;
    private Long memberId;
    private Boolean isDeleted;

    public static ChatMember create(Long chatRoomId, Long memberId) {
        return ChatMember.builder()
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .isDeleted(false)
                .build();
    }

    public void exit() {
        isDeleted = true;
    }

    public void restore() {
        isDeleted = false;
    }
}
