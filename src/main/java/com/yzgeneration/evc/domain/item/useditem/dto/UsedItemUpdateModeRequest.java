package com.yzgeneration.evc.domain.item.useditem.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.validator.Validatable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UsedItemUpdateModeRequest implements Validatable {

    @NotBlank
    private String transactionMode;

    public UsedItemUpdateModeRequest(String transactionMode) {
        this.transactionMode = transactionMode;
        valid();
    }

    @Override
    public void valid() {
        EnumValidator.validate(TransactionMode.class, "transactionMode", transactionMode);
    }
}
