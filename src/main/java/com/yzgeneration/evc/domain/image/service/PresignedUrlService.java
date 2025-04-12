package com.yzgeneration.evc.domain.image.service;

import com.yzgeneration.evc.domain.image.dto.ImageRequest;
import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.implement.ImageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {
    private final ImageHandler imageHandler;

    public List<ImageResponse> generateForItem(ImageRequest imageRequest) {
        String prefix = imageRequest.getPrefix();
        return imageRequest.getImageNames().stream().map(imageName ->
                imageHandler.getPresignedURLForUpload(prefix, imageName)
        ).toList();
    }

    public ImageResponse generateForProfile(String fileName) {
        return imageHandler.getPresignedURLForUpload("profile", fileName);
    }
}
