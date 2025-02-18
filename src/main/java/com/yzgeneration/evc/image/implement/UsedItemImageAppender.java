package com.yzgeneration.evc.image.implement;

import com.yzgeneration.evc.image.model.UsedItemImage;
import com.yzgeneration.evc.image.service.port.S3FileHandler;
import com.yzgeneration.evc.image.service.port.UsedItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsedItemImageAppender {
    private final UsedItemImageRepository usedItemImageRepository;
    private final S3FileHandler s3FileHandler;

    public List<String> createUsedItemImages(Long itemId, List<MultipartFile> imageFiles) {
        List<String> imageURLs = imageFiles.stream().map(s3FileHandler::uploadFileToS3).toList();

        imageURLs.forEach(imageURL -> usedItemImageRepository.save(UsedItemImage.create(itemId, imageURL)));

        return imageURLs;
    }
}
