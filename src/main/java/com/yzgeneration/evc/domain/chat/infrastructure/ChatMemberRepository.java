package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatMember;

import java.util.List;

public interface ChatMemberRepository {
    ChatMember save(ChatMember chatMember);
    List<ChatMember> saveAll(List<ChatMember> chatMembers);
}
