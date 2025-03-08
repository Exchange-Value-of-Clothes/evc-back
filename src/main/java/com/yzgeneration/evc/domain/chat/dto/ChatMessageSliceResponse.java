package com.yzgeneration.evc.domain.chat.dto;

import com.yzgeneration.evc.common.dto.SliceResponse;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

@Getter
public class ChatMessageSliceResponse extends SliceResponse<ChatMessageResponse> {

    private Long chatRoomId;

    public ChatMessageSliceResponse(Long chatRoomId, Slice<ChatMessageResponse> slice, LocalDateTime cursor) {
        super(slice, cursor);
        this.chatRoomId = chatRoomId;
    }
}
