package com.yzgeneration.evc.domain.item.auctionitem.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.validator.Validatable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class AuctionItemRequest {

    @Getter
    @NoArgsConstructor
    public static class CreateAuctionItemRequest implements Validatable {

        private String title;

        private String transactionType;

        private String category;

        private String content;

        private int startPrice;

        private int bidPrice;

        private List<String> imageNames = new ArrayList<>();

        @JsonCreator
        public CreateAuctionItemRequest(String title, String transactionType, String category, String content,
                                        int startPrice, int bidPrice, List<String> imageNames) {
            this.title = title;
            this.transactionType = transactionType;
            this.category = category;
            this.content = content;
            this.startPrice = startPrice;
            this.bidPrice = bidPrice;
            this.imageNames = imageNames;
            valid();
        }

        @Override
        public void valid() {
            EnumValidator.validate(TransactionType.class, "transactionType", transactionType);
        }
    }
}
