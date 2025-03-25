package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;

public interface ImageHandler {
    ImageResponse getPresignedURLForUpload(String prefix, String fileName);
}
