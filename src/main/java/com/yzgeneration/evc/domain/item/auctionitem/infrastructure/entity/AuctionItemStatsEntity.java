package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItemStats;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuctionItemStatsEntity {

    private int viewCount;

    private int participantCount;

    public static AuctionItemStatsEntity from(AuctionItemStats auctionItemStats){
        return AuctionItemStatsEntity.builder()
                .viewCount(auctionItemStats.getViewCount())
                .participantCount(auctionItemStats.getParticipantCount())
                .build();
    }

    public AuctionItemStats toModel(){
        return AuctionItemStats.builder()
                .viewCount(viewCount)
                .participantCount(participantCount)
                .build();
    }
}
