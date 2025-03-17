package com.yzgeneration.evc.domain.useditem.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.validator.Validatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UsedItemRequest {
    @Getter
    @NoArgsConstructor
    public static class CreateUsedItemRequest implements Validatable {

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

        private List<String> imageNames;

        @JsonCreator
        public CreateUsedItemRequest(String title, String category, String content, int price, String transactionType, String transactionMode, List<String> imageNames) {
            this.title = title;
            this.category = category;
            this.content = content;
            this.price = price;
            this.transactionType = transactionType;
            this.transactionMode = transactionMode;
            this.imageNames = imageNames;
            valid();
        }

        @Override
        public void valid() {
            EnumValidator.validate(TransactionType.class, "trasactionType", transactionType);
            EnumValidator.validate(TransactionMode.class, "transactionMode", transactionMode);
        }
    }
}
