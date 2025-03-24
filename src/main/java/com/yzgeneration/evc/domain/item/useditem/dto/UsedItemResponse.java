package com.yzgeneration.evc.domain.item.useditem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


public class UsedItemResponse {

    @Getter
    @Builder
    public static class GetUsedItemsResponse {

        @JsonUnwrapped
        private List<GetUsedItemsDetails> loadUsedItemDetails;

        private Boolean isLast;
    }

    @Getter
    @Builder
    public static class GetUsedItemsDetails {

        private Long usedItemId;

        private String title;

        private int price;

        private TransactionMode transactionMode;

        private TransactionStatus transactionStatus;

        private String imageName;

        private int likeCount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createAt;

        private ItemStatus itemStatus;

        public static GetUsedItemsDetails create(UsedItem usedItem, String imageName) {
            return GetUsedItemsDetails.builder()
                    .usedItemId(usedItem.getId())
                    .title(usedItem.getItemDetails().getTitle())
                    .price(usedItem.getItemDetails().getPrice())
                    .transactionMode(usedItem.getUsedItemTransaction().getTransactionMode())
                    .transactionStatus(usedItem.getUsedItemTransaction().getTransactionStatus())
                    .imageName(imageName)
                    .likeCount(usedItem.getItemStats().getLikeCount())
                    .createAt(usedItem.getCreatedAt())
                    .itemStatus(usedItem.getItemStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class GetUsedItemResponse {

        private String title;

        private String category;

        private String content;

        private int price;

        private TransactionType transactionType;

        private TransactionMode transactionMode;

        private TransactionStatus transactionStatus;

        private List<String> imageNames;

        private int viewCount;

        private int likeCount;

        private int chattingCount;

        // nickName 중북이기에 해당 id값을 이용해서 판매자의 상점 드갈 때 사용
        private Long marketMemberId;

        private String marketNickname;

        private Boolean isOwned;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createAt;

        private ItemStatus itemStatus;

        //TODO 회원의 프로필 사진 response

        public static GetUsedItemResponse create(UsedItem usedItem, List<String> imageNames, String marketNickname, Long memberId) {
            return GetUsedItemResponse.builder()
                    .title(usedItem.getItemDetails().getTitle())
                    .category(usedItem.getItemDetails().getCategory())
                    .content(usedItem.getItemDetails().getContent())
                    .price(usedItem.getItemDetails().getPrice())
                    .transactionType(usedItem.getUsedItemTransaction().getTransactionType())
                    .transactionMode(usedItem.getUsedItemTransaction().getTransactionMode())
                    .transactionStatus(usedItem.getUsedItemTransaction().getTransactionStatus())
                    .imageNames(imageNames)
                    .viewCount(usedItem.getItemStats().getViewCount())
                    .likeCount(usedItem.getItemStats().getLikeCount())
                    .chattingCount(usedItem.getItemStats().getChattingCount())
                    .marketMemberId(usedItem.getMemberId())
                    .marketNickname(marketNickname)
                    .isOwned(usedItem.getMemberId().equals(memberId))
                    .createAt(usedItem.getCreatedAt())
                    .itemStatus(usedItem.getItemStatus())
                    .build();
        }
    }
}
