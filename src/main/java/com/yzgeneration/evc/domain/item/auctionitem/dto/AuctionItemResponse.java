package com.yzgeneration.evc.domain.item.auctionitem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionItemResponse {

    @Getter
    @AllArgsConstructor
    public static class GetAuctionItemResponse {

        @JsonUnwrapped
        private AuctionItemDetailsResponse auctionItemDetailsResponse;

        @JsonUnwrapped
        private AuctionItemStatsResponse auctionItemStatsResponse;

        @Setter
        private List<String> imageNameList = new ArrayList<>();

        private TransactionType transactionType;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endTime;

        private int currentPrice;

        private Long marketMemberId;

        private String marketNickname;

        private Boolean isOwned;

        private ItemStatus itemStatus;
    }

    @Getter
    @AllArgsConstructor
    public static class AuctionItemDetailsResponse {

        private String title;

        private String category;

        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class AuctionItemStatsResponse {

        private int viewCount;

        private int likeCount;

        private int participantCount;
    }
}
