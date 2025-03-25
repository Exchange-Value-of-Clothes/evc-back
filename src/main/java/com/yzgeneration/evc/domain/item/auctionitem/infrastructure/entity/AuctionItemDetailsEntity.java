package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItemDetails;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuctionItemDetailsEntity {

    private String title;

    private String category;

    private String content;

    public static AuctionItemDetailsEntity from(AuctionItemDetails auctionItemDetails) {
        return AuctionItemDetailsEntity.builder()
                .title(auctionItemDetails.getTitle())
                .category(auctionItemDetails.getCategory())
                .content(auctionItemDetails.getContent())
                .build();
    }

    public AuctionItemDetails toModel() {
        return AuctionItemDetails.builder()
                .title(title)
                .category(category)
                .content(content)
                .build();
    }
}
