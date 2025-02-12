package com.yzgeneration.evc.domain.chat.implement;

import com.yzgeneration.evc.domain.chat.infrastructure.ChatRoomRepository;
import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomProcessor {

    private final ChatRoomRepository chatRoomRepository;

    public void save(Long ownerId, Long senderId) {
        chatRoomRepository.save(ChatRoom.create(ownerId, senderId));
    }
}
