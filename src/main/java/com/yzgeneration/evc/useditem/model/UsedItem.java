package com.yzgeneration.evc.useditem.model;

import com.yzgeneration.evc.useditem.enums.UsedItemStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UsedItem {
    private Long id;
    private String title;
    private String category;
    private String content;
    private String price;
    private UsedItemTransaction usedItemTransaction;
    private UsedItemStatus usedItemStatus;
    private int viewCount;
    private int likeCount;
    private int chattingCount;
    private String imagePath;
    private LocalDateTime createdAt;
}
