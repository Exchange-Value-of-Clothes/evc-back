package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "auction_items")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuctionItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Embedded
    private AuctionItemDetailsEntity auctionItemDetailsEntity;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Long viewCount;

    @Embedded
    private AuctionItemPriceDetailsEntity auctionItemPriceDetailsEntity;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus; //경매 ONGOING, COMPLETE만 사용

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    private LocalDateTime startTime;

    // startTime + 24h
    private LocalDateTime endTime;

    public static AuctionItemEntity from(AuctionItem auctionItem) {
        return AuctionItemEntity.builder()
                .memberId(auctionItem.getMemberId())
                .auctionItemDetailsEntity(AuctionItemDetailsEntity.from(auctionItem.getAuctionItemDetails()))
                .transactionType(auctionItem.getTransactionType())
                .auctionItemPriceDetailsEntity(AuctionItemPriceDetailsEntity.from(auctionItem.getAuctionItemPriceDetails()))
                .transactionStatus(auctionItem.getTransactionStatus())
                .itemStatus(auctionItem.getItemStatus())
                .startTime(auctionItem.getStartTime())
                .endTime(auctionItem.getEndTime())
                .build();
    }

    public AuctionItem toModel() {
        return AuctionItem.builder()
                .id(id)
                .memberId(memberId)
                .auctionItemDetails(auctionItemDetailsEntity.toModel())
                .transactionStatus(transactionStatus)
                .auctionItemPriceDetails(auctionItemPriceDetailsEntity.toModel())
                .transactionStatus((transactionStatus))
                .itemStatus(itemStatus)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
