package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatMember;

public interface ChatMemberRepository {
    ChatMember save(ChatMember chatMember);
}
