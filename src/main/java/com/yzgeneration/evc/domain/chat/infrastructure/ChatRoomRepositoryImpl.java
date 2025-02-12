package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomJpaRepository.save(ChatRoomEntity.create(chatRoom)).toModel();
    }
}
