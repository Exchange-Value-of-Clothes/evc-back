package com.yzgeneration.evc.domain.image.implement;

public interface ImageHandler {
    String getPresignedURLForUpload(String prefix, String fileName);
    String getImageUrl(String fileName);
}
