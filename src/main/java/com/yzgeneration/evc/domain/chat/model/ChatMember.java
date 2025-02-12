package com.yzgeneration.evc.domain.chat.model;

import java.time.LocalDateTime;

public class ChatMember {
    private Long id;
    private Long chatRoomId;
    private Long memberId;
    private LocalDateTime lastEntryTime;
}
