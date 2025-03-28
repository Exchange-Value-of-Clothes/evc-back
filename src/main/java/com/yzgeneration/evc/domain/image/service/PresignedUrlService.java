package com.yzgeneration.evc.domain.image.service;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.implement.ImageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {
    private final ImageHandler imageHandler;

    public List<ImageResponse> generatePresignedURL(String prefix, List<String> imageNames) {
        return imageNames.stream().map(imageName ->
                imageHandler.getPresignedURLForUpload(prefix, imageName)
        ).toList();
    }

    public ImageResponse generateForProfile(String fileName) {
        return imageHandler.getPresignedURLForUpload("profile", fileName);
    }
}
