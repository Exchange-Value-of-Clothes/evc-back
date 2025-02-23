package com.yzgeneration.evc.domain.useditem.model;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionStatue;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsedItemTransaction {

    private TransactionType transactionType;

    private TransactionMode transactionMode;

    private TransactionStatue transactionStatue;

    public static UsedItemTransaction create(CreateUsedItemRequest createUsedItemRequest) {
        return UsedItemTransaction.builder()
                .transactionType(TransactionType.valueOf(createUsedItemRequest.getTransactionType()))
                .transactionMode(TransactionMode.valueOf(createUsedItemRequest.getTransactionMode()))
                .transactionStatue(TransactionStatue.ONGOING)
                .build();
    }
}
