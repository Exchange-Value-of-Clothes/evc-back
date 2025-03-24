package com.yzgeneration.evc.domain.item.useditem.model;

import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDetails {

    private String title;

    private String category;

    private String content;

    private int price;

    public static ItemDetails create(CreateUsedItemRequest createUsedItemRequest) {
        return ItemDetails.builder()
                .title(createUsedItemRequest.getTitle())
                .category(createUsedItemRequest.getCategory())
                .content(createUsedItemRequest.getContent())
                .price(createUsedItemRequest.getPrice())
                .build();
    }
}