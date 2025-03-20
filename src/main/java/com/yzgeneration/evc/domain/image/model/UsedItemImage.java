package com.yzgeneration.evc.domain.image.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsedItemImage {

    private Long id;

    private Long usedItemId;

    private String imageURL;

    private boolean isThumbnail;

    public static UsedItemImage create(Long usedItemId, String imageURL, boolean isThumbnail) {
        return UsedItemImage.builder()
                .usedItemId(usedItemId)
                .imageURL(imageURL)
                .isThumbnail(isThumbnail)
                .build();
    }
}
