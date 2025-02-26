package com.yzgeneration.evc.domain.chat.implement;

import com.yzgeneration.evc.domain.chat.infrastructure.ChatConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatConnectionManager {

    private final ChatConnectionRepository chatConnectionRepository;

    public void connectToChatRoom(Long chatRoomId, Long memberId) {
        chatConnectionRepository.connect(chatRoomId, memberId);
    }

    public void exitChatRoom(Long chatRoomId, Long memberId) {
        chatConnectionRepository.disconnect(chatRoomId, memberId);
    }

    public boolean isChatPartnerConnected(Long chatRoomId) {
        return chatConnectionRepository.getOnlineMemberCount(chatRoomId) > 1;
    }
}
