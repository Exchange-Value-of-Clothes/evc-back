package com.yzgeneration.evc.domain.chat.implement;

import com.yzgeneration.evc.domain.chat.dto.Chatting;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatMessageRepository;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageProcessor {

    private final ChatMessageRepository chatMessageRepository;

    public void saveMessage(Long chatRoomId, Long memberId, Chatting chatting, boolean chatPartnerExist) {
        chatMessageRepository.save(ChatMessage.create(chatRoomId, memberId, chatting.getContent(),chatPartnerExist));
    }
}
