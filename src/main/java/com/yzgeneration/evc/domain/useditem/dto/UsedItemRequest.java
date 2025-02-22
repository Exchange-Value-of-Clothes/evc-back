package com.yzgeneration.evc.domain.useditem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UsedItemRequest {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUsedItemRequest {

        @NotNull
        private Long memberId;

        @NotBlank(message = "제목은 필수항목입니다.")
        private String title;

        @NotBlank(message = "카테고리는 필수항목입니다.")
        private String category;

        @NotBlank(message = "내용은 필수항목입니다.")
        private String content;

        @NotNull(message = "가격은 필수항목입니다.")
        private int price;

        @NotNull(message = "거래유형을 선택해주세요.")
        private String transactionType;

        @NotNull(message = "거래방법을 선택해주세요.")
        private String transactionMode;

    }
}
