package com.yzgeneration.evc.fixture.usedItem;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.fixture.Fixture;

import java.util.List;

public abstract class UsedItemFixture {

    public static CreateUsedItemRequest fixCreateUsedItemRequest() {
        return Fixture.fixtureMonkey.giveMeBuilder(CreateUsedItemRequest.class)
                .set("title", "Test UsedItem")
                .set("category", "top shirt")
                .set("content", "buy right now")
                .set("price", 10000)
                .set("transactionType", "DIRECT")
                .set("transactionMode", "BUY")
                .set("imageNames", List.of("1234.jpg", "5678.jpg"))
                .sample();
    }
}
