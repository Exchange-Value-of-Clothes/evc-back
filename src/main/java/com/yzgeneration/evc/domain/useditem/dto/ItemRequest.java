package com.yzgeneration.evc.domain.useditem.dto;

import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import lombok.Getter;

public class ItemRequest {
    @Getter
    public static class CreateItem {
        private Long memberId;
        private String title;
        private String category;
        private String content;
        private int price;
        private CreateTransaction createTransaction;
    }

    @Getter
    public static class CreateTransaction {
        private TransactionType transactionType;
        private TransactionMode transactionMode;
    }
}
