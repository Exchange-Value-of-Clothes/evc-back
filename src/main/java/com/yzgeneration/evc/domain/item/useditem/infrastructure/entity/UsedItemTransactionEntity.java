package com.yzgeneration.evc.domain.item.useditem.infrastructure.entity;

import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItemTransaction;
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
    private TransactionStatus transactionStatus;

    public static UsedItemTransactionEntity from(UsedItemTransaction usedItemTransaction) {
        return UsedItemTransactionEntity.builder()
                .transactionType(usedItemTransaction.getTransactionType())
                .transactionMode(usedItemTransaction.getTransactionMode())
                .transactionStatus(usedItemTransaction.getTransactionStatus()).build();
    }

    public UsedItemTransaction toModel() {
        return UsedItemTransaction.builder()
                .transactionType(transactionType)
                .transactionMode(transactionMode)
                .transactionStatus(transactionStatus)
                .build();
    }
}
