package com.yzgeneration.evc.domain.image.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageResponse {
    private String presignedURL;
    private String imageName;
}
