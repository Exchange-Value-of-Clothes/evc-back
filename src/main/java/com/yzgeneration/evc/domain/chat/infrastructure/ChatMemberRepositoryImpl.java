package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatMemberRepositoryImpl implements ChatMemberRepository {

    private final ChatMemberJpaRepository chatMemberJpaRepository;

    @Override
    public ChatMember save(ChatMember chatMember) {
        return chatMemberJpaRepository.save(ChatMemberEntity.from(chatMember)).toModel();
    }

    @Override
    public List<ChatMember> saveAll(List<ChatMember> chatMembers) {
        List<ChatMemberEntity> chatMemberEntities = chatMemberJpaRepository.saveAll(chatMembers.stream()
                .map(ChatMemberEntity::from).toList());
        return chatMemberEntities.stream().map(ChatMemberEntity::toModel).collect(Collectors.toList());
    }
}
