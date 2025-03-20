package com.yzgeneration.evc.mock.usedItem;

import com.yzgeneration.evc.domain.image.implement.ImageHandler;

public class SpyImageHandler implements ImageHandler {

    @Override
    public String getPresignedURLForUpload(String prefix, String fileName) {
        return String.format("%s/%s", prefix, "1234" + "-" + fileName);
    }

    @Override
    public String getImageUrl(String fileName) {
        return "http://localhost/images/1234.jpg";
    }
}
