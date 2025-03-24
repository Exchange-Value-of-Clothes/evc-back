package com.yzgeneration.evc.mock.usedItem;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.implement.ImageHandler;

public class SpyImageHandler implements ImageHandler {

    @Override
    public ImageResponse getPresignedURLForUpload(String prefix, String fileName) {
        String imageName = String.format("%s/%s", prefix, "1234" + "-" + fileName);
        return ImageResponse.builder()
                .presignedURL("http://localhost:8080/image/" + imageName)
                .imageName(imageName)
                .build();
    }
}
