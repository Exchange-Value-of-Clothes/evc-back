package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.model.UsedItemImage;
import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UsedItemImageAppender {
    private final UsedItemImageRepository usedItemImageRepository;
    private final ImageHandler imageHandler;

    public void createUsedItemImages(Long itemId, List<String> imageNames) {
        List<String> imageURLs = imageNames.stream().map(imageHandler::getImageUrl).toList();
        //첫 번째 사진 == 썸네일 사진
        String thumbnailImage = imageURLs.get(0);
        usedItemImageRepository.saveAll(imageURLs.stream().map(imageURL -> UsedItemImage.create(itemId, imageURL, Objects.equals(thumbnailImage, imageURL))).toList());
    }
}