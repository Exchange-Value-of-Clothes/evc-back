package com.yzgeneration.evc.fixture;

import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;

import java.util.List;

import static com.yzgeneration.evc.fixture.Fixture.fixtureMonkey;

public class UsedItemFixture {

    public static CreateUsedItemRequest fixCreateUsedItemRequest() {
        return fixtureMonkey.giveMeBuilder(CreateUsedItemRequest.class)
                .set("title", "title")
                .set("category", "category")
                .set("content", "content")
                .set("price", 5000)
                .set("transactionType", "DIRECT")
                .set("transactionMode", "BUY")
                .set("imageNames", List.of("imageName.jpg"))
                .sample();
    }
}
