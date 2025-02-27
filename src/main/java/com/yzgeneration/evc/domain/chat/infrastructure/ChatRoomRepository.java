package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatRoom;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);

    ChatRoom getById(Long id);
}
