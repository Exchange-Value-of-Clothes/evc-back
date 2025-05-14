package com.yzgeneration.evc.domain.item.useditem.model;

import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
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

    private ItemStatus itemStatus;

    private ItemStats itemStats;

    private LocalDateTime createdAt;

    public static UsedItem create(Long memberId, CreateUsedItemRequest createUsedItemRequest, LocalDateTime createAt) {
        return UsedItem.builder()
                .memberId(memberId)
                .itemDetails(ItemDetails.create(createUsedItemRequest))
                .usedItemTransaction(UsedItemTransaction.create(createUsedItemRequest))
                .itemStatus(ItemStatus.ACTIVE)
                .itemStats(ItemStats.create())
                .createdAt(createAt)
                .build();
    }

    public void updateTransactionStatus(TransactionStatus transactionMode) {
        this.usedItemTransaction.updateTransactionStatus(transactionMode);
    }

    public void updateItemStatus(ItemStatus itemStatus){
        this.itemStatus = itemStatus;
    }
}
