package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final ChatMessageMongoRepository chatMessageMongoRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageMongoRepository.save(ChatMessageDocument.from(chatMessage)).toModel();
    }

    @Override
    public boolean checkUnread(Long chatRoomId, LocalDateTime lastEntryTime) {
        return chatMessageMongoRepository.existsByChatRoomIdAndCreatedAtAfter(chatRoomId, lastEntryTime);
    }

}
