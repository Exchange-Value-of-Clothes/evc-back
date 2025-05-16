package com.yzgeneration.evc.domain.item.useditem.model;

import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsedItemTransaction {

    private TransactionType transactionType;

    private TransactionMode transactionMode;

    private TransactionStatus transactionStatus;

    public static UsedItemTransaction create(CreateUsedItemRequest createUsedItemRequest) {
        return UsedItemTransaction.builder()
                .transactionType(TransactionType.valueOf(createUsedItemRequest.getTransactionType()))
                .transactionMode(TransactionMode.valueOf(createUsedItemRequest.getTransactionMode()))
                .transactionStatus(TransactionStatus.ONGOING)
                .build();
    }

    public void updateTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void update(TransactionType transactionType, TransactionMode transactionMode) {
        this.transactionType = transactionType;
        this.transactionMode = transactionMode;
    }
}
