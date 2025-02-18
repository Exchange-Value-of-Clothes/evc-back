package com.yzgeneration.evc.useditem.infrastructure.entity;

import com.yzgeneration.evc.useditem.enums.TransactionMode;
import com.yzgeneration.evc.useditem.enums.TransactionStatue;
import com.yzgeneration.evc.useditem.enums.TransactionType;
import com.yzgeneration.evc.useditem.model.UsedItemTransaction;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsedItemTransactionEntity {
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionMode transactionMode;

    @Enumerated(EnumType.STRING)
    private TransactionStatue transactionStatue;

    public static UsedItemTransactionEntity from(UsedItemTransaction usedItemTransaction) {
        return UsedItemTransactionEntity.builder()
                .transactionType(usedItemTransaction.getTransactionType())
                .transactionMode(usedItemTransaction.getTransactionMode())
                .transactionStatue(usedItemTransaction.getTransactionStatue()).build();
    }

    public UsedItemTransaction toModel() {
        return UsedItemTransaction.builder()
                .transactionType(transactionType)
                .transactionMode(transactionMode)
                .transactionStatue(transactionStatue)
                .build();
    }
}
