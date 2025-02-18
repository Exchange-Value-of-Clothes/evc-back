package com.yzgeneration.evc.fixture.usedItem;

import com.yzgeneration.evc.domain.useditem.dto.ItemRequest.CreateItemDetails;
import com.yzgeneration.evc.domain.useditem.dto.ItemRequest.CreateTransaction;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.fixture.Fixture;

public abstract class UsedItemFixture {
    public static CreateItemDetails fixCreateItemDetails() {
        return Fixture.fixtureMonkey.giveMeBuilder(CreateItemDetails.class)
                .set("title", "Test UsedItem")
                .set("category", "top shirt")
                .set("content", "buy right now")
                .set("price", 10000)
                .sample();
    }

    public static CreateTransaction fixCreateTransaction() {
        return Fixture.fixtureMonkey.giveMeBuilder(CreateTransaction.class)
                .set("transactionType", TransactionType.DIRECT)
                .set("transactionMode", TransactionMode.BUY)
                .sample();
    }

    public static CreateUsedItem fixCreateUsedItem() {
        return Fixture.fixtureMonkey.giveMeBuilder(CreateUsedItem.class)
                .set("memberId", 1L)
                .set("createItemDetails", fixCreateItemDetails())
                .set("createTransaction", fixCreateTransaction())
                .sample();
    }
}
