package com.yzgeneration.evc.domain.item.auctionitem.model;

import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionItemDetails {

    private String title;

    private String category;

    private String content;

    public static AuctionItemDetails create(CreateAuctionItemRequest createAuctionItemRequest) {
        return AuctionItemDetails.builder()
                .title(createAuctionItemRequest.getTitle())
                .category(createAuctionItemRequest.getCategory())
                .content(createAuctionItemRequest.getContent())
                .build();
    }

    public void update(String title, String category, String content) {
        this.title = title;
        this.category = category;
        this.content = content;
    }
}
