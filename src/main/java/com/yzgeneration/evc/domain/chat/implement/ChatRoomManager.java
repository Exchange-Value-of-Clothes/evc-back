package com.yzgeneration.evc.domain.chat.implement;

import com.yzgeneration.evc.domain.chat.infrastructure.ChatMemberRepository;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatRoomRepository;
import com.yzgeneration.evc.domain.chat.model.ChatMember;
import com.yzgeneration.evc.domain.chat.model.ChatRoom;
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

    public ChatRoom getOrCreate(Long usedItemId, Long ownerId, Long participantId) {
        if (Objects.equals(ownerId, participantId)) throw new CustomException(ErrorCode.SELF_CHAT_NOT_ALLOWED);
        return chatRoomRepository.findByUsedItemIdAndParticipantId(usedItemId, participantId)
                .orElseGet(() -> {
                    ChatRoom newChatRoom = chatRoomRepository.save(ChatRoom.create(usedItemId, ownerId, participantId));
                    chatMemberRepository.saveAll(List.of(
                            ChatMember.create(newChatRoom.getId(), ownerId),
                            ChatMember.create(newChatRoom.getId(), participantId)
                    ));
                    return newChatRoom;
                });
    }


}
