package com.yzgeneration.evc.domain.chat.implement;

import com.yzgeneration.evc.domain.chat.infrastructure.ChatMemberRepository;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatRoomRepository;
import com.yzgeneration.evc.domain.chat.model.ChatMember;
import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ChatRoomManager {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;

    public ChatRoom getOrCreate(Long itemId, ItemType itemType, Long ownerId, Long participantId) {
        if (Objects.equals(ownerId, participantId)) throw new CustomException(ErrorCode.SELF_CHAT_NOT_ALLOWED);
        ChatRoom chatRoom = chatRoomRepository.findByItemIdAndItemTypeAndParticipantId(itemId, itemType, participantId)
                .orElseGet(() -> {
                    ChatRoom newChatRoom = chatRoomRepository.save(ChatRoom.create(itemId, ownerId, participantId, itemType));
                    saveChatMembers(ownerId, participantId, newChatRoom); // 새로운 채팅방일 때만 멤버 저장
                    return newChatRoom;
                });
        restoreDeletedMembers(chatRoom.getId(), ownerId, participantId);
        return chatRoom;
    }


    public void deleteChatMember(Long chatRoomId, Long memberId) {
        ChatMember chatMember = chatMemberRepository.get(chatRoomId, memberId);
        chatMember.exit();
        chatMemberRepository.save(chatMember);
    }

    public ChatRoom getById(Long chatRoomId) {
        return chatRoomRepository.getById(chatRoomId);
    }

    private void saveChatMembers(Long ownerId, Long participantId, ChatRoom newChatRoom) {
        chatMemberRepository.saveAll(List.of(
                ChatMember.create(newChatRoom.getId(), ownerId),
                ChatMember.create(newChatRoom.getId(), participantId)
        ));
    }

    private void restoreDeletedMembers(Long chatRoomId, Long ownerId, Long participantId) {
        List<ChatMember> deletedMembers = chatMemberRepository.getDeletedChatMembers(chatRoomId, List.of(ownerId, participantId));
        if (!deletedMembers.isEmpty()) {
            for (ChatMember chatMember : deletedMembers) {
                chatMember.restore();
            }
            chatMemberRepository.saveAll(deletedMembers);
        }
    }





}
