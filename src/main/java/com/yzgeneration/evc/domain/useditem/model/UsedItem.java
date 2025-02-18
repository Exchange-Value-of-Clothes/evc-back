package com.yzgeneration.evc.domain.useditem.model;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
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

    public static UsedItem create(CreateUsedItem createUsedItem, LocalDateTime createAt) {
        return UsedItem.builder()
                .itemDetails(ItemDetails.create(createUsedItem.getCreateItemDetails()))
                .usedItemTransaction(UsedItemTransaction.create(createUsedItem.getCreateTransaction()))
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .itemStats(ItemStats.create())
                .createdAt(createAt)
                .build();
    }
}
