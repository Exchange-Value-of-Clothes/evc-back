package com.yzgeneration.evc.domain.image.service;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.implement.S3ImageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3ImageHandler s3ImageHandler;

    public List<ImageResponse> generatePresignedURL(String prefix, List<MultipartFile> imageFiles) {
        return imageFiles.stream().map(imageFile ->
                ImageResponse.builder()
                        .presignedURL(s3ImageHandler.getPresignedURLForUpload(prefix, imageFile.getOriginalFilename()))
                        .imageName(imageFile.getOriginalFilename())
                        .build()
        ).toList();
    }
}
