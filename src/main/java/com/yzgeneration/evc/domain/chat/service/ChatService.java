package com.yzgeneration.evc.domain.chat.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.*;
import com.yzgeneration.evc.domain.chat.implement.*;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatMessageRepository;

import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import com.yzgeneration.evc.domain.chat.model.ChatRoom;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


import static com.yzgeneration.evc.domain.chat.SessionConstant.CHAT_ROOM_KEY;
import static com.yzgeneration.evc.domain.chat.SessionConstant.MEMBER_KEY;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomManager chatRoomManager;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatConnectionManager chatConnectionManager;
    private final SessionAttributeAccessor sessionAttributeAccessor;
    private final RabbitTemplate rabbitTemplate;

    public ChatMessageSliceResponse getChatRoomByTradeRequest(Long itemId, ItemType itemType, Long ownerId, Long participantId) {
        ChatRoom chatRoom = chatRoomManager.getOrCreate(itemId, itemType,ownerId, participantId);
        return chatMessageRepository.getLastMessages(participantId, chatRoom.getId(), LocalDateTime.now(), ownerId, itemType, itemId, ownerId);
    }

    public SliceResponse<ChatRoomListResponse> getChatRooms(Long memberId, LocalDateTime cursor) {
        return chatMessageRepository.getChatRooms(memberId, cursor);
    }

    public ChatMessageSliceResponse getChatRoomByListSelection(Long chatRoomId, LocalDateTime cursor, Long memberId) {
        ChatRoom chatRoom = chatRoomManager.getById(chatRoomId);
        Long otherPersonId = !Objects.equals(chatRoom.getOwnerId(), memberId) ? chatRoom.getOwnerId() : chatRoom.getParticipantId();
        return chatMessageRepository.getLastMessages(memberId, chatRoomId, cursor, chatRoom.getOwnerId(), chatRoom.getItemType(), chatRoom.getItemId(), otherPersonId);
    }

    public void send(StompHeaderAccessor accessor, Chatting chatting) {
        Long chatRoomId = sessionAttributeAccessor.getById(accessor, CHAT_ROOM_KEY);
        Long memberId = sessionAttributeAccessor.getById(accessor, MEMBER_KEY);
        boolean isChatPartnerConnected = chatConnectionManager.isChatPartnerConnected(chatRoomId);
        chatMessageRepository.save(ChatMessage.create(chatRoomId, memberId, chatting.getMsg(), isChatPartnerConnected, chatting.getCreatedAt()));
        rabbitTemplate.convertAndSend("chat.topic", "room." + chatRoomId, ChattingToListener.of(chatRoomId, memberId, chatting.getMsg(), chatting.getCreatedAt(),isChatPartnerConnected));
    }

    public void exit(Long chatRoomId, Long memberId) {
        chatRoomManager.deleteChatMember(chatRoomId, memberId);
    }
}