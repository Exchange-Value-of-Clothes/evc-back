package com.yzgeneration.evc.domain.item.auctionitem.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionRoom {

    private Long id;

    private Long auctionItemId;

    private Boolean isDeleted;

    public static AuctionRoom create(Long auctionItemId) {
        return AuctionRoom.builder()
                .auctionItemId(auctionItemId)
                .isDeleted(false)
                .build();
    }
}
