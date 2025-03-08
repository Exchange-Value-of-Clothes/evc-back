package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findByUsedItemIdAndParticipantId(Long usedItemId, Long participantId);
}
