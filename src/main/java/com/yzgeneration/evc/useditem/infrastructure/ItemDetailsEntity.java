package com.yzgeneration.evc.useditem.infrastructure;

import com.yzgeneration.evc.useditem.model.ItemDetails;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemDetailsEntity {
    private String title;
    private String category;
    private String content;
    private int price;

    public static ItemDetailsEntity from(ItemDetails itemDetails) {
        return ItemDetailsEntity.builder()
                .title(itemDetails.getTitle())
                .category(itemDetails.getCategory())
                .content(itemDetails.getContent())
                .price(itemDetails.getPrice())
                .build();
    }

    public ItemDetails toModel() {
        return ItemDetails.builder()
                .title(title)
                .category(category)
                .content(content)
                .price(price)
                .build();
    }
}
