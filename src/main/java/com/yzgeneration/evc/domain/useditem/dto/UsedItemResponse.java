package com.yzgeneration.evc.domain.useditem.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionStatue;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


public class UsedItemResponse {

    @Getter
    @AllArgsConstructor
    public static class CreateUsedItemResponse {

        private Long memberId;

        private Long usedItemId;
    }

    @Getter
    @Builder
    public static class LoadUsedItemsResponse {

        @JsonUnwrapped
        private List<LoadUsedItemsDetails> loadUsedItemDetails;

        private Boolean isLast;
    }

    @Getter
    @Builder
    public static class LoadUsedItemsDetails {

        private Long usedItemId;

        private String title;

        private int price;

        private TransactionMode transactionMode;

        private List<String> imageURLs;

        private int likeCount;

        private LocalDateTime createAt;

        private UsedItemStatus usedItemStatus;
    }

    @Getter
    @Builder
    public static class LoadUsedItemResponse {

        private String title;

        private String category;

        private String content;

        private int price;

        private TransactionType transactionType;

        private TransactionMode transactionMode;

        private TransactionStatue transactionStatue;

        private List<String> imageURLs;

        private int viewCount;

        private int likeCount;

        private int chattingCount;

        // nickName 중북이기에 해당 id값을 이용해서 판매자의 상점 드갈 때 사용
        private Long memberId;

        private String nickName;

        private Boolean isOwned;

        private LocalDateTime createAt;

        private UsedItemStatus usedItemStatus;
    }
}
