package com.yzgeneration.evc.fixture;

import com.yzgeneration.evc.domain.chat.dto.ChatEnter;

public final class ChatFixture extends Fixture {
    private ChatFixture() {}

    public static ChatEnter chatEnter() {
        return fixtureMonkey.giveMeBuilder(ChatEnter.class)
                .set("usedItemId", 1L)
                .set("ownerId", 1L)
                .sample();
    }
}
