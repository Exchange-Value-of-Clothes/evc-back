package com.yzgeneration.evc.domain.my.dto;

import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.validator.Validatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class MyAuctionItemUpdateRequest implements Validatable {
    @NotBlank
    private String title;

    @NotBlank
    private String category;

    @NotBlank
    private String content;

    @NotNull
    private int startPrice;

    @NotNull
    private int bidPrice;

    private String transactionType;

    private List<String> imageNames = new ArrayList<>();

    @Override
    public void valid() {
        EnumValidator.validate(TransactionType.class, "transactionType", transactionType);
    }
}