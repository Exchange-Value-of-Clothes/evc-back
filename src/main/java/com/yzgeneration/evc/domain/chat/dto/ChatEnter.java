package com.yzgeneration.evc.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.validator.Validatable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatEnter implements Validatable {
    private Long itemId;
    private String itemType;
    private Long ownerId;

    @JsonCreator
    public ChatEnter(
            @JsonProperty("itemId") Long itemId, @JsonProperty("itemType") String itemType, @JsonProperty("ownerId") Long ownerId) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.ownerId = ownerId;
        valid();
    }

    @Override
    public void valid() {
        EnumValidator.validate(ItemType.class, "itemType", itemType);
    }
}
