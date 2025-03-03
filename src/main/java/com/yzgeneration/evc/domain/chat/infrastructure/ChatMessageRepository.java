package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;

import java.time.LocalDateTime;

public interface ChatMessageRepository {
    ChatMessage save(ChatMessage chatMessage);
    SliceResponse<ChatRoomListResponse> getLastMessages(Long memberId);

}
