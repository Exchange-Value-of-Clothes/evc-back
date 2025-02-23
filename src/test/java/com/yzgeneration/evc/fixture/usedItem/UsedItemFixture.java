package com.yzgeneration.evc.fixture.usedItem;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.fixture.Fixture;

public abstract class UsedItemFixture {

    public static CreateUsedItemRequest fixCreateUsedItemRequest() {
        return Fixture.fixtureMonkey.giveMeBuilder(CreateUsedItemRequest.class)
                .set("memberId", 1L)
                .set("title", "Test UsedItem")
                .set("category", "top shirt")
                .set("content", "buy right now")
                .set("price", 10000)
                .set("transactionType", "DIRECT")
                .set("transactionMode", "BUY")
                .sample();
    }
}
