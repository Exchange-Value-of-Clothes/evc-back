package com.yzgeneration.evc.fixture.usedItem;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.yzgeneration.evc.domain.useditem.dto.ItemRequest.CreateItemDetails;
import com.yzgeneration.evc.domain.useditem.dto.ItemRequest.CreateTransaction;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;

import java.util.List;

public abstract class UsedItemFixture {

    public static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(new FailoverIntrospector(
                    List.of(BuilderArbitraryIntrospector.INSTANCE, FieldReflectionArbitraryIntrospector.INSTANCE)
            ))
            .defaultNotNull(true)
            .build();

    public static CreateItemDetails fixCreateItemDetails() {
        return fixtureMonkey.giveMeBuilder(CreateItemDetails.class)
                .set("title", "Test UsedItem")
                .set("category", "top shirt")
                .set("content", "buy right now")
                .set("price", 10000)
                .sample();
    }

    public static CreateTransaction fixCreateTransaction() {
        return fixtureMonkey.giveMeBuilder(CreateTransaction.class)
                .set("transactionType", TransactionType.DIRECT)
                .set("transactionMode", TransactionMode.BUY)
                .sample();
    }

    public static CreateUsedItem fixCreateUsedItem() {
        return fixtureMonkey.giveMeBuilder(CreateUsedItem.class)
                .set("memberId", 1L)
                .set("createItemDetails", fixCreateItemDetails())
                .set("createTransaction", fixCreateTransaction())
                .sample();
    }
}
