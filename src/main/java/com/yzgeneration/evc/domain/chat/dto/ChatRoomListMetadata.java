package com.yzgeneration.evc.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomListMetadata {
    private Long chatRoomId;
    private Long otherMemberId;
    private String nickname;
    private String profileImageUrl;
}
