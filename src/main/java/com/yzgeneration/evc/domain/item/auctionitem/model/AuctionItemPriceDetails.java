package com.yzgeneration.evc.domain.item.auctionitem.model;

import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionItemPriceDetails {

    private int startPrice;

    private int currentPrice;

    private int bidPrice;

    public static AuctionItemPriceDetails create(CreateAuctionItemRequest createAuctionItemRequest) {
        return AuctionItemPriceDetails.builder()
                .startPrice(createAuctionItemRequest.getStartPrice())
                //생성 직후의 currentPrice == startPrice
                .currentPrice(createAuctionItemRequest.getStartPrice())
                .bidPrice(createAuctionItemRequest.getBidPrice())
                .build();
    }
}
