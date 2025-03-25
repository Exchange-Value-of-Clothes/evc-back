package com.yzgeneration.evc.domain.item.auctionitem.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionItemStats {

    private int viewCount;

    private int participantCount;

    public static AuctionItemStats create() {
        return AuctionItemStats.builder().build();
    }
}
