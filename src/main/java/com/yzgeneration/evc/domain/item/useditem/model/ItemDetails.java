package com.yzgeneration.evc.domain.item.useditem.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDetails {

    private String title;

    private String category;

    private String content;

    private int price;

    public static ItemDetails create(String title, String category, String content, int price) {
        return ItemDetails.builder()
                .title(title)
                .category(category)
                .content(content)
                .price(price)
                .build();
    }

    public void update(String title, String category, String content, int price) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.price = price;
    }
}