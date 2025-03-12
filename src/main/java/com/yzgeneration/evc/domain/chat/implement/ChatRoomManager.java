package com.yzgeneration.evc.domain.chat.implement;

import com.yzgeneration.evc.domain.chat.infrastructure.ChatMemberRepository;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatRoomRepository;
import com.yzgeneration.evc.domain.chat.model.ChatMember;
import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatRoomManager {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;

    public ChatRoom getOrCreate(Long usedItemId, Long ownerId, Long participantId) {
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
