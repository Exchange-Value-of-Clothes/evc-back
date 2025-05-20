package com.yzgeneration.evc.domain.my.dto;

import com.yzgeneration.evc.domain.item.enums.TransactionMode;
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
public class MyUsedItemUpdateRequest implements Validatable {
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

    private List<String> addImageNames = new ArrayList<>();

    private List<String> removeImageNames = new ArrayList<>();

    private String thumbnailImage;

    @Override
    public void valid() {
        EnumValidator.validate(TransactionType.class, "transactionType", transactionType);
        EnumValidator.validate(TransactionMode.class, "transactionMode", transactionMode);
    }
}
