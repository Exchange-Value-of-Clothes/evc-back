package com.yzgeneration.evc.image.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UsedItemImage extends BaseImage {
    private Long id;
    private Long usedItemId;
}
