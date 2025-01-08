package com.yzgeneration.evc.useditem.model;

import com.yzgeneration.evc.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.useditem.enums.UsedItemStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class UsedItem {
    private Long id;
    private String title;
    private String category;
    private String content;
    private int price;
    private UsedItemTransaction usedItemTransaction;
    private UsedItemStatus usedItemStatus;
    private int viewCount;
    private int likeCount;
    private int chattingCount;
    private List<String> imageName;
    private LocalDateTime createdAt;

    public static UsedItem create(CreateUsedItem createUsedItem) {
        return UsedItem.builder()
                .title(createUsedItem.getTitle())
                .category(createUsedItem.getCategory())
                .content(createUsedItem.getContent())
                .price(createUsedItem.getPrice())
                .usedItemTransaction(UsedItemTransaction.create(createUsedItem.getCreateTransaction()))
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .viewCount(0)
                .likeCount(0)
                .chattingCount(0)
                // [notice] 이미지 처리 aws s3 연동 이후에 진행
                .imageName(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
