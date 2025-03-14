package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.model.UsedItemImage;
import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsedItemImageAppender {
    private final UsedItemImageRepository usedItemImageRepository;
    private final S3ImageHandler s3ImageHandler;

    public void createUsedItemImages(Long itemId, List<String> imageNames) {
        List<String> imageURLs = imageNames.stream().map(s3ImageHandler::getImageUrl).toList();
        usedItemImageRepository.saveAll(imageURLs.stream().map(imageURL -> UsedItemImage.create(itemId, imageURL)).toList());
    }
}