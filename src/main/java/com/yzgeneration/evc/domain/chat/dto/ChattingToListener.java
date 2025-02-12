package com.yzgeneration.evc.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingToListener {
    private Long chatRoomId;
    private Long memberId;
    private String content;
    private boolean chatPartnerExist;

    public static ChattingToListener of(Long chatRoomId, Long memberId, String content, boolean chatPartnerExist) {
        return new ChattingToListener(chatRoomId, memberId, content, chatPartnerExist);
    }
}
