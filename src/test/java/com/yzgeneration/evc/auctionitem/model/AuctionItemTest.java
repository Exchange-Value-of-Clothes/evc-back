package com.yzgeneration.evc.auctionitem.model;

import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.yzgeneration.evc.fixture.AuctionItemFixture.fixCreateAuctionItemRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class AuctionItemTest {

    @Test
    @DisplayName("경매상품 생성")
    void createAuctionItem() {
        //given
        CreateAuctionItemRequest createAuctionItemRequest = fixCreateAuctionItemRequest();

        //when
        AuctionItem auctionItem = AuctionItem.create(1L, createAuctionItemRequest);

        //then
        assertThat(auctionItem.getMemberId().equals(1L));
        assertThat(auctionItem.getAuctionItemDetails().getTitle().equals("title"));
        assertThat(auctionItem.getAuctionItemDetails().getCategory().equals("category"));

    }
}
