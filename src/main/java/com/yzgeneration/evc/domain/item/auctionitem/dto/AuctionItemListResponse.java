package com.yzgeneration.evc.domain.item.auctionitem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class AuctionItemListResponse {

    @Getter
    @AllArgsConstructor
    public static class GetAuctionItemListResponse {

        private Long auctionItemId;

        private String title;

        @JsonUnwrapped
        private AuctionItemPriceDetailsResponse auctionItemPriceDetailsResponse;

        private String imageName; //썸네일용

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endTime;

        private int point;
    }

    @Getter
    @AllArgsConstructor
    public static class AuctionItemPriceDetailsResponse {

        private int startPrice;

        private int currentPrice;

        private int bidPrice;
    }

}
