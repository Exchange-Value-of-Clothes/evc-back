package com.yzgeneration.evc.domain.item.useditem.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.validator.Validatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class UsedItemRequest {
    @Getter
    @NoArgsConstructor
    public static class CreateUsedItemRequest implements Validatable {

        @NotBlank
        private String title;

        @NotBlank
        private String category;

        @NotBlank
        private String content;

        @NotNull
        private int price;

        private String transactionType;

        private String transactionMode;

        private List<String> imageNames = new ArrayList<>();

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
            EnumValidator.validate(TransactionType.class, "transactionType", transactionType);
            EnumValidator.validate(TransactionMode.class, "transactionMode", transactionMode);
        }
    }
}
