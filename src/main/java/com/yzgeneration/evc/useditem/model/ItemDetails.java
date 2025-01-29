package com.yzgeneration.evc.useditem.model;

import com.yzgeneration.evc.useditem.dto.ItemRequest.CreateItemDetails;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDetails {
    private String title;
    private String category;
    private String content;
    private int price;

    public static ItemDetails create(CreateItemDetails createItemDetails) {
        return ItemDetails.builder()
                .title(createItemDetails.getTitle())
                .category(createItemDetails.getCategory())
                .content(createItemDetails.getContent())
                .price(createItemDetails.getPrice())
                .build();
    }
}
