package com.yzgeneration.evc.domain.image.service;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.implement.ImageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageHandler imageHandler;

    public List<ImageResponse> generatePresignedURL(String prefix, List<MultipartFile> imageFiles) {
        return imageFiles.stream().map(imageFile ->
                ImageResponse.builder()
                        .presignedURL(imageHandler.getPresignedURLForUpload(prefix, imageFile.getOriginalFilename()))
                        .imageName(imageFile.getOriginalFilename())
                        .build()
        ).toList();
    }
}
