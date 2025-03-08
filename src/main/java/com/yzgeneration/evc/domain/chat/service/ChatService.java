package com.yzgeneration.evc.domain.chat.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.*;
import com.yzgeneration.evc.domain.chat.implement.*;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatMemberRepository;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatMessageRepository;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatRoomRepository;
import com.yzgeneration.evc.domain.chat.model.ChatMember;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.yzgeneration.evc.domain.chat.SessionConstant.CHAT_ROOM_KEY;
import static com.yzgeneration.evc.domain.chat.SessionConstant.MEMBER_KEY;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatConnectionManager chatConnectionManager;
    private final SessionAttributeAccessor sessionAttributeAccessor;
    private final RabbitTemplate rabbitTemplate;

    public ChatMessageSliceResponse getChatRoomByTradeRequest(Long usedItemId, Long ownerId, Long participantId) { // TODO implement
        if (Objects.equals(ownerId, participantId)) throw new CustomException(ErrorCode.SELF_CHAT_NOT_ALLOWED);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByUsedItemIdAndParticipantId(usedItemId, participantId);
        if(chatRoom.isEmpty()) {
            Long chatRoomId = chatRoomRepository.save(ChatRoom.create(usedItemId, ownerId, participantId)).getId();
            chatMemberRepository.saveAll(List.of(ChatMember.create(chatRoomId, ownerId), ChatMember.create(chatRoomId, participantId)));
            return chatMessageRepository.getLastMessages(chatRoomId, LocalDateTime.now());
        } else {
            return chatMessageRepository.getLastMessages(chatRoom.get().getId(), LocalDateTime.now());
        }
    }

    public SliceResponse<ChatRoomListResponse> getChatRooms(Long memberId, LocalDateTime cursor) {
        return chatMessageRepository.getChatRooms(memberId, cursor);
    }

    public ChatMessageSliceResponse getChatRoomByListSelection(Long chatRoomId, LocalDateTime cursor) {
        return chatMessageRepository.getLastMessages(chatRoomId, cursor);
    }

    public void send(StompHeaderAccessor accessor, Chatting chatting) {
        Long chatRoomId = sessionAttributeAccessor.getById(accessor, CHAT_ROOM_KEY);
        Long memberId = sessionAttributeAccessor.getById(accessor, MEMBER_KEY);
        boolean isChatPartnerConnected = chatConnectionManager.isChatPartnerConnected(chatRoomId);
        chatMessageRepository.save(ChatMessage.create(chatRoomId, memberId, chatting.getContent(), isChatPartnerConnected));
        rabbitTemplate.convertAndSend("chat.topic", "room."+chatRoomId, ChattingToListener.of(chatRoomId, memberId, chatting.getContent(), isChatPartnerConnected)); // chat.topic이라는 Topic Exchange로 메시지를 보냄. → Routing Key가 room.{chatRoomId}이므로 해당 키를 구독해야 메시지를 받을 수 있음.
    }

    public void disconnect(StompHeaderAccessor accessor) {
        Long chatRoomId = sessionAttributeAccessor.getById(accessor, CHAT_ROOM_KEY);
        Long memberId = sessionAttributeAccessor.getById(accessor, MEMBER_KEY);
        chatConnectionManager.exitChatRoom(chatRoomId, memberId);
    }

}
