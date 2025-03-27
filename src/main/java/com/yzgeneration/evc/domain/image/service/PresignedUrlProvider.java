package com.yzgeneration.evc.domain.image.service;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.implement.ImageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresignedUrlProvider {
    private final ImageHandler imageHandler;

    public List<ImageResponse> generatePresignedURL(String prefix, List<MultipartFile> imageFiles) {
        return imageFiles.stream().map(imageFile ->
                imageHandler.getPresignedURLForUpload(prefix, imageFile.getOriginalFilename())
        ).toList();
    }

    public ImageResponse generateForProfile(String fileName) {
        return imageHandler.getPresignedURLForUpload("profile",fileName);
    }
}
