package com.yzgeneration.evc.domain.item.auctionitem.model;

import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuctionItem {
    private Long id;

    private Long memberId;

    private AuctionItemDetails auctionItemDetails;

    private AuctionItemStats auctionItemStats;

    private AuctionItemPriceDetails auctionItemPriceDetails;

    private TransactionStatus transactionStatus;

    private ItemStatus itemStatus;

    private LocalDateTime startTime;

    // startTime + 24h
    private LocalDateTime endTime;

    public static AuctionItem create(Long memberId, CreateAuctionItemRequest createAuctionItemRequest) {
        LocalDateTime now = LocalDateTime.now();
        return AuctionItem.builder()
                .memberId(memberId)
                .auctionItemDetails(AuctionItemDetails.create(createAuctionItemRequest))
                .auctionItemStats(AuctionItemStats.create())
                .auctionItemPriceDetails(AuctionItemPriceDetails.create(createAuctionItemRequest))
                .transactionStatus(TransactionStatus.ONGOING)
                .itemStatus(ItemStatus.ACTIVE)
                .startTime(now)
                .endTime(now.plusDays(1))
                .build();
    }
}
