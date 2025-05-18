package com.yzgeneration.evc.domain.delivery.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.validator.Validatable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryPrepare implements Validatable {

    private String itemType;
    @NotNull
    private Long itemId;
    @NotNull
    private Long buyerId;
    @NotNull
    private Long sellerId;

    @JsonCreator
    public DeliveryPrepare(@JsonProperty("itemType") String itemType, @JsonProperty("itemId") Long itemId,
                           @JsonProperty("buyerId") Long buyerId, @JsonProperty("sellerId") Long sellerId) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        valid();
    }

    @Override
    public void valid() {
        EnumValidator.validate(ItemType.class, "itemType", itemType);
    }
}
