package com.yzgeneration.evc.domain.useditem.model;

import com.yzgeneration.evc.domain.useditem.dto.ItemRequest.CreateTransaction;
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

    public static UsedItemTransaction create(CreateTransaction createTransaction) {
        return UsedItemTransaction.builder()
                .transactionType(createTransaction.getTransactionType())
                .transactionMode(createTransaction.getTransactionMode())
                .transactionStatue(TransactionStatue.ONGOING)
                .build();
    }
}
