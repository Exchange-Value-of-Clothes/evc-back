package com.yzgeneration.evc.fixture;

import com.yzgeneration.evc.domain.chat.dto.ChatEnter;
import com.yzgeneration.evc.domain.chat.model.ChatMember;

public final class ChatFixture extends Fixture {
    private ChatFixture() {}

    public static ChatEnter chatEnter() {
        return fixtureMonkey.giveMeBuilder(ChatEnter.class)
                .set("itemId", 1L)
                .set("itemType", "USEDITEM")
                .set("ownerId", 1L)
                .sample();
    }

    public static ChatMember chatMember(Long chatRoomId, Long memberId) {
        return fixtureMonkey.giveMeBuilder(ChatMember.class)
                .set("chatRoomId", chatRoomId)
                .set("memberId", memberId)
                .set("isDeleted", false)
                .sample();
    }
}
