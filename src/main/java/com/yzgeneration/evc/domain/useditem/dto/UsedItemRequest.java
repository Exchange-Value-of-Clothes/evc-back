package com.yzgeneration.evc.domain.useditem.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class UsedItemRequest {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateUsedItem {

        @NotNull
        @JsonUnwrapped
        private ItemRequest.CreateItemDetails createItemDetails;

        @NotNull
        @JsonUnwrapped
        private ItemRequest.CreateTransaction createTransaction;
    }
}
