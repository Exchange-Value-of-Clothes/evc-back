package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMemberRepositoryImpl implements ChatMemberRepository {

    private final ChatMemberJpaRepository chatMemberJpaRepository;

    @Override
    public ChatMember save(ChatMember chatMember) {
        return chatMemberJpaRepository.save(ChatMemberEntity.from(chatMember)).toModel();
    }
}
