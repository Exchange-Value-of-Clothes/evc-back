package com.yzgeneration.evc.domain.useditem.dto;

import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class ItemRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateItemDetails {
        @NotBlank(message = "제목은 필수항목입니다.")
        private String title;

        @NotBlank(message = "카테고리는 필수항목입니다.")
        private String category;

        @NotBlank(message = "내용은 필수항목입니다.")
        private String content;

        @NotNull(message = "가격은 필수항목입니다.")
        private int price;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTransaction {
        @NotNull(message = "거래유형을 선택해주세요.")
        private TransactionType transactionType;

        @NotNull(message = "거래방법을 선택해주세요.")
        private TransactionMode transactionMode;
    }
}
