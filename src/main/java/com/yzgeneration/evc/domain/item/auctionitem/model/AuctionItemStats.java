package com.yzgeneration.evc.domain.item.auctionitem.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionItemStats {

    private Long viewCount;

    private Long participantCount;

    public static AuctionItemStats create() {
        return AuctionItemStats.builder().build();
    }
}
