package com.yzgeneration.evc.mock.image;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.implement.ImageHandler;

public class SpyImageHandler implements ImageHandler {
    @Override
    public ImageResponse getPresignedURLForUpload(String prefix, String fileName) {
        return ImageResponse.builder()
                .presignedURL("https://www.abc.com")
                .imageName(prefix + "/abc-" + fileName)
                .build();
    }
}
