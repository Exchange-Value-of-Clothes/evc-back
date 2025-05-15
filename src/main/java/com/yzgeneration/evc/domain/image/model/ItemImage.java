package com.yzgeneration.evc.domain.image.model;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemImage {

    private Long id;

    private Long itemId;

    private ItemType itemType;

    private String imageName;

    private boolean isThumbnail;

    public static ItemImage create(Long itemId, ItemType itemType, String imageName, boolean isThumbnail) {
        return ItemImage.builder()
                .itemId(itemId)
                .itemType(itemType)
                .imageName(imageName)
                .isThumbnail(isThumbnail)
                .build();
    }
}
