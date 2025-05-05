package com.yzgeneration.evc.domain.item.auctionitem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class AuctionItemsResponse {

    @Getter
    @AllArgsConstructor
    public static class GetAuctionItemsResponse {

        private Long auctionItemId;

        private String title;

        private String category;

        @JsonUnwrapped
        private AuctionItemPriceDetailResponse auctionItemPriceDetailResponse;

        @Setter
        private Long participantCount;

        private String imageName; //썸네일용

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endTime;

        private int point;
    }

    @Getter
    @AllArgsConstructor
    public static class AuctionItemPriceDetailResponse {

        private int startPrice;

        private int currentPrice;

        private int bidPrice;
    }

    @Getter
    @AllArgsConstructor
    public static class GetMyOrMemberAuctionItemsResponse {

        private Long auctionItemId;

        private String title;

        private int price;

        private TransactionMode transactionMode;

        private TransactionStatus transactionStatus;

        private String imageName;

        private Long likeCount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createAt;

        private ItemStatus itemStatus;
    }

}
