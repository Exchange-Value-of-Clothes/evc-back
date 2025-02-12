package com.yzgeneration.evc.domain.chat.service;

import com.yzgeneration.evc.domain.chat.dto.Chatting;
import com.yzgeneration.evc.domain.chat.dto.ChattingToListener;
import com.yzgeneration.evc.domain.chat.implement.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import static com.yzgeneration.evc.common.SessionConstant.CHAT_ROOM_KEY;
import static com.yzgeneration.evc.common.SessionConstant.MEMBER_KEY;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomProcessor chatRoomProcessor;
    private final ChatMessageProcessor chatMessageProcessor;
    private final ChatConnectionManager chatConnectionManager;
    private final SessionAttributeAccessor sessionAttributeAccessor;
    private final RabbitTemplate rabbitTemplate;

    public void save(Long ownerId, Long senderId) {
        chatRoomProcessor.save(ownerId, senderId);
    }

    public void send(StompHeaderAccessor accessor, Chatting chatting) {
        Long chatRoomId = sessionAttributeAccessor.getById(accessor, CHAT_ROOM_KEY);
        Long memberId = sessionAttributeAccessor.getById(accessor, MEMBER_KEY);
        boolean chatPartnerExists = chatConnectionManager.doseChatPartnerExist(chatRoomId);
        chatMessageProcessor.saveMessage(chatRoomId, memberId, chatting, chatPartnerExists);
        rabbitTemplate.convertAndSend("chat.topic", "room."+chatRoomId, ChattingToListener.of(chatRoomId, memberId, chatting.getContent(), chatPartnerExists));
    }

    public void disconnect(StompHeaderAccessor accessor) {
        Long chatRoomId = sessionAttributeAccessor.getById(accessor, CHAT_ROOM_KEY);
        Long memberId = sessionAttributeAccessor.getById(accessor, MEMBER_KEY);
        chatConnectionManager.exitChatRoom(chatRoomId, memberId);
    }
}
