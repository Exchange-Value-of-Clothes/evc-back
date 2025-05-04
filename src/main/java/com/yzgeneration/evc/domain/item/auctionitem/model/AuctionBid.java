package com.yzgeneration.evc.domain.item.auctionitem.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuctionBid {

    private String id;

    private Long auctionRoomId;

    private Long bidderId;

    private int price;

    private LocalDateTime createAt;

    public static AuctionBid create(Long auctionRoomId, Long bidderId, int price) {
        return AuctionBid.builder()
                .auctionRoomId(auctionRoomId)
                .bidderId(bidderId)
                .price(price)
                .createAt(LocalDateTime.now())
                .build();
    }
}
