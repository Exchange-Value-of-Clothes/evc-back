package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItemPriceDetails;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuctionItemPriceDetailsEntity {

    private int startPrice;

    private int currentPrice;

    private int bidPrice;

    public static AuctionItemPriceDetailsEntity from(AuctionItemPriceDetails auctionItemPriceDetails) {
        return AuctionItemPriceDetailsEntity.builder()
                .startPrice(auctionItemPriceDetails.getStartPrice())
                .currentPrice(auctionItemPriceDetails.getCurrentPrice())
                .bidPrice(auctionItemPriceDetails.getBidPrice())
                .build();
    }

    public AuctionItemPriceDetails toModel() {
        return AuctionItemPriceDetails.builder()
                .startPrice(startPrice)
                .currentPrice(currentPrice)
                .bidPrice(bidPrice)
                .build();
    }
}
