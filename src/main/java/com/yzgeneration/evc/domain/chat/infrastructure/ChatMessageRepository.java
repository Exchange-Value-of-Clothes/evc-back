package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatMessageSliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import com.yzgeneration.evc.domain.item.enums.ItemType;

import java.time.LocalDateTime;

public interface ChatMessageRepository {
    ChatMessage save(ChatMessage chatMessage);
    SliceResponse<ChatRoomListResponse> getChatRooms(Long memberId, LocalDateTime cursor);
    ChatMessageSliceResponse getLastMessages(Long memberId, Long chatRoomId, LocalDateTime cursor, Long ownerId, ItemType itemType, Long itemId, Long otherPersonId);
}
