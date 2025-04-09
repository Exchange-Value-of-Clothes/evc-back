package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionBid;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "auction_bid")
public class AuctionBidDocument {

    @Id
    private String id;

    private Long auctionRoomId;

    private Long bidderId;

    private int price;

    public static AuctionBidDocument from(AuctionBid auctionBid) {
        return AuctionBidDocument.builder()
                .auctionRoomId(auctionBid.getAuctionRoomId())
                .bidderId(auctionBid.getBidderId())
                .price(auctionBid.getPrice())
                .build();
    }

    public AuctionBid toModel() {
        return AuctionBid.builder()
                .id(id)
                .auctionRoomId(auctionRoomId)
                .bidderId(bidderId)
                .price(price)
                .build();
    }
}
