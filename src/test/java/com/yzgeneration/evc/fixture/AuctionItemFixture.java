package com.yzgeneration.evc.fixture;

import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;

import java.util.List;

import static com.yzgeneration.evc.fixture.Fixture.fixtureMonkey;

public class AuctionItemFixture {

    public static CreateAuctionItemRequest fixCreateAuctionItemRequest() {
        return fixtureMonkey.giveMeBuilder(CreateAuctionItemRequest.class)
                .set("title", "title")
                .set("transactionType", "DIRECT")
                .set("category", "category")
                .set("content", "content")
                .set("startPrice", 5000)
                .set("bidPrice", 1000)
                .set("imageNames", List.of("imageName.jpg"))
                .sample();
    }
}
