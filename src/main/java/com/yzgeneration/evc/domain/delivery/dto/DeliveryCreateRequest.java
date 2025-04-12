package com.yzgeneration.evc.domain.delivery.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.validator.Validatable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryCreateRequest implements Validatable {


    private String itemType;
    private Long itemId; // name, price
    private String size;  // xs, s, m, l
    private Long receiverId;

    @JsonCreator
    public DeliveryCreateRequest(@JsonProperty("itemType") String itemType, @JsonProperty("itemId") Long itemId,
                                 @JsonProperty("size") String size, @JsonProperty("receiverId") Long receiverId) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.size = size;
        this.receiverId = receiverId;
        valid();
    }

    @Override
    public void valid() {
        EnumValidator.validate(ItemType.class, "itemType", itemType);
    }
}
