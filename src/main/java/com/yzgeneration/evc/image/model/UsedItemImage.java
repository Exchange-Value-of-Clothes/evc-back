package com.yzgeneration.evc.image.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsedItemImage {
    private Long id;
    private Long usedItemId;
    private String imageURL;

    public static UsedItemImage create(Long usedItemId, String imageURL) {
        return UsedItemImage.builder()
                .usedItemId(usedItemId)
                .imageURL(imageURL)
                .build();
    }
}
