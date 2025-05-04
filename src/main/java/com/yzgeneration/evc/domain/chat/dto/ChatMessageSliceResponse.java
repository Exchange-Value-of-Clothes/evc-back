package com.yzgeneration.evc.domain.chat.dto;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

@Getter
public class ChatMessageSliceResponse extends SliceResponse<ChatMessageResponse> {

    private Long chatRoomId;
    private Long yourId;
    private Long ownerId;
    private String itemType;
    private Long itemId;

    public ChatMessageSliceResponse(Long chatRoomId, Long yourId, Long ownerId, Slice<ChatMessageResponse> slice, LocalDateTime cursor,
                                    ItemType itemType, Long itemId) {
        super(slice, cursor);
        this.chatRoomId = chatRoomId;
        this.yourId = yourId;
        this.ownerId = ownerId;
        this.itemType = itemType.name();
        this.itemId = itemId;
    }

}
