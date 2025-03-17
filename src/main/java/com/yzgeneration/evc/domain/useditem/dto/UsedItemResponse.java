package com.yzgeneration.evc.domain.useditem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionStatue;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
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

        private TransactionStatue transactionStatue;

        private String imageURL;

        private int likeCount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createAt;

        private UsedItemStatus usedItemStatus;

        public static LoadUsedItemsDetails create(UsedItem usedItem, String imageURL) {
            return LoadUsedItemsDetails.builder()
                    .usedItemId(usedItem.getId())
                    .title(usedItem.getItemDetails().getTitle())
                    .price(usedItem.getItemDetails().getPrice())
                    .transactionMode(usedItem.getUsedItemTransaction().getTransactionMode())
                    .transactionStatue(usedItem.getUsedItemTransaction().getTransactionStatue())
                    .imageURL(imageURL)
                    .likeCount(usedItem.getItemStats().getLikeCount())
                    .createAt(usedItem.getCreatedAt())
                    .usedItemStatus(usedItem.getUsedItemStatus())
                    .build();
        }
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
        private Long marketMemberId;

        private String marketNickName;

        private Boolean isOwned;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createAt;

        private UsedItemStatus usedItemStatus;

        //TODO 회원의 프로필 사진 response

        public static LoadUsedItemResponse create(UsedItem usedItem, List<String> imageURLs, String marketNickName, Long memberId) {
            return LoadUsedItemResponse.builder()
                    .title(usedItem.getItemDetails().getTitle())
                    .category(usedItem.getItemDetails().getCategory())
                    .content(usedItem.getItemDetails().getContent())
                    .price(usedItem.getItemDetails().getPrice())
                    .transactionType(usedItem.getUsedItemTransaction().getTransactionType())
                    .transactionMode(usedItem.getUsedItemTransaction().getTransactionMode())
                    .transactionStatue(usedItem.getUsedItemTransaction().getTransactionStatue())
                    .imageURLs(imageURLs)
                    .viewCount(usedItem.getItemStats().getViewCount())
                    .likeCount(usedItem.getItemStats().getLikeCount())
                    .chattingCount(usedItem.getItemStats().getChattingCount())
                    .marketMemberId(usedItem.getMemberId())
                    .marketNickName(marketNickName)
                    .isOwned(usedItem.getMemberId().equals(memberId))
                    .createAt(usedItem.getCreatedAt())
                    .usedItemStatus(usedItem.getUsedItemStatus())
                    .build();
        }
    }
}
