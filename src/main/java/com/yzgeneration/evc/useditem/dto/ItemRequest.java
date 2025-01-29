package com.yzgeneration.evc.useditem.dto;

import com.yzgeneration.evc.useditem.enums.TransactionMode;
import com.yzgeneration.evc.useditem.enums.TransactionType;
import lombok.Getter;

public class ItemRequest {
    @Getter
    public static class CreateItem {
        private Long memberId;
        private CreateItemDetails createItemDetails;
        private CreateTransaction createTransaction;
    }

    @Getter
    public static class CreateItemDetails{
        private String title;
        private String category;
        private String content;
        private int price;
    }

    @Getter
    public static class CreateTransaction {
        private TransactionType transactionType;
        private TransactionMode transactionMode;
    }
}
