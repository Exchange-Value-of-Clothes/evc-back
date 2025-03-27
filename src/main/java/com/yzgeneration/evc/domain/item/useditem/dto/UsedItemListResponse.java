package com.yzgeneration.evc.domain.item.useditem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class UsedItemListResponse {

    @Getter
    @AllArgsConstructor
    public static class GetUsedItemListResponse {

        private Long usedItemId;

        private String title;

        private int price;

        private TransactionMode transactionMode;

        private TransactionStatus transactionStatus;

        private String imageName;

        private int likeCount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createAt;

        private ItemStatus itemStatus;
    }
}
