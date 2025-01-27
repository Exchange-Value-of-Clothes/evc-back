package com.yzgeneration.evc.domain.image.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class BaseImage {
    private String imageName;
    private String imageURL;
}
