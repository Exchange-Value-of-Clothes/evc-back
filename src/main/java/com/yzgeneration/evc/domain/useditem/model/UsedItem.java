package com.yzgeneration.evc.domain.useditem.model;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UsedItem {

    private Long id;

    private Long memberId;

    private ItemDetails itemDetails;

    private UsedItemTransaction usedItemTransaction;

    private UsedItemStatus usedItemStatus;

    private ItemStats itemStats;

    private LocalDateTime createdAt;

    public static UsedItem create(CreateUsedItemRequest createUsedItemRequest, LocalDateTime createAt) {
        return UsedItem.builder()
                .memberId(createUsedItemRequest.getMemberId())
                .itemDetails(ItemDetails.create(createUsedItemRequest))
                .usedItemTransaction(UsedItemTransaction.create(createUsedItemRequest))
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .itemStats(ItemStats.create())
                .createdAt(createAt)
                .build();
    }
}
