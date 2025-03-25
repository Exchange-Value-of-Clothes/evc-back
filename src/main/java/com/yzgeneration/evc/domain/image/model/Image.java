package com.yzgeneration.evc.domain.image.model;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Image {

    private Long id;

    private Long itemId;

    private ItemType itemType;

    private String imageName;

    private boolean isThumbnail;

    public static Image create(Long itemId, ItemType itemType, String imageName, boolean isThumbnail) {
        return Image.builder()
                .itemId(itemId)
                .itemType(itemType)
                .imageName(imageName)
                .isThumbnail(isThumbnail)
                .build();
    }
}
