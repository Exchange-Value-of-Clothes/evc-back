package com.yzgeneration.evc.useditem.model;

import com.yzgeneration.evc.useditem.dto.ItemRequest.CreateTransaction;
import com.yzgeneration.evc.useditem.enums.TransactionMode;
import com.yzgeneration.evc.useditem.enums.TransactionStatue;
import com.yzgeneration.evc.useditem.enums.TransactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
