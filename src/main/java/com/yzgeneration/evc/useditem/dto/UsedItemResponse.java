package com.yzgeneration.evc.useditem.dto;

import com.yzgeneration.evc.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.useditem.model.UsedItem;
import com.yzgeneration.evc.useditem.model.UsedItemTransaction;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public class UsedItemResponse {
    private final Long memberId;
    private final String title;
    private final String category;
    private final String content;
    private final int price;
    private final UsedItemTransaction usedItemTransaction;
    private final UsedItemStatus usedItemStatus;
    private final int viewCount;
    private final int likeCount;
    private final int chattingCount;
    private final List<String> imageName;
    private final LocalDateTime createAt;

    public static UsedItemResponse from(UsedItem usedItem) {
        return UsedItemResponse.builder()
                .memberId(usedItem.getId())
                .title(usedItem.getTitle())
                .category(usedItem.getCategory())
                .content(usedItem.getContent())
                .price(usedItem.getPrice())
                .usedItemTransaction(usedItem.getUsedItemTransaction())
                .usedItemStatus(usedItem.getUsedItemStatus())
                .viewCount(usedItem.getViewCount())
                .likeCount(usedItem.getLikeCount())
                .chattingCount(usedItem.getChattingCount())
                .imageName(usedItem.getImageName())
                .createAt(usedItem.getCreatedAt())
                .build();
    }
}
