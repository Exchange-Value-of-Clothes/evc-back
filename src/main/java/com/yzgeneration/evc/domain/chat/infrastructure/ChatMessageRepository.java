package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatMessage;

import java.time.LocalDateTime;

public interface ChatMessageRepository {
    ChatMessage save(ChatMessage chatMessage);
    boolean checkUnread(Long chatRoomId, LocalDateTime lastEntryTime);
}
