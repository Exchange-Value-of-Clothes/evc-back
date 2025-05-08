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
    private Long otherPersonId;
    private String itemType;
    private Long itemId;
    private String transactionType;
    private String title;
    private int price;

    public ChatMessageSliceResponse(Long chatRoomId, Long yourId, Long ownerId, Slice<ChatMessageResponse> slice, LocalDateTime cursor, Long otherPersonId,
                                    ItemType itemType, Long itemId,
                                    String transactionType, String title, int price) {
        super(slice, cursor);
        this.chatRoomId = chatRoomId;
        this.yourId = yourId;
        this.ownerId = ownerId;
        this.otherPersonId = otherPersonId;
        this.itemType = itemType.name();
        this.itemId = itemId;
        this.transactionType = transactionType;
        this.title = title;
        this.price = price;
    }

}
