package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import com.yzgeneration.evc.domain.image.enums.ItemType;

import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findByItemIdAndItemTypeAndParticipantId(Long itemId, ItemType itemType, Long participantId);
    ChatRoom getById(Long id);
}
