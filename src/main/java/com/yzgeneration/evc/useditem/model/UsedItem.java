package com.yzgeneration.evc.useditem.model;

import com.yzgeneration.evc.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.useditem.enums.UsedItemStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UsedItem {
    private Long id;
    private ItemDetails itemDetails;
    private UsedItemTransaction usedItemTransaction;
    private UsedItemStatus usedItemStatus;
    private ItemStats itemStats;
    private LocalDateTime createdAt;

    public static UsedItem create(CreateUsedItem createUsedItem) {
        return UsedItem.builder()
                .itemDetails(ItemDetails.create(createUsedItem.getCreateItemDetails()))
                .usedItemTransaction(UsedItemTransaction.create(createUsedItem.getCreateTransaction()))
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .itemStats(ItemStats.create())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
