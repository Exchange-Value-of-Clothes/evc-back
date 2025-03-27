package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.model.ItemImage;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ItemImageAppender {
    private final ItemImageRepository itemImageRepository;

    public void createItemImages(Long itemId, ItemType itemType, List<String> imageNames) {
        //첫 번째 사진 == 썸네일 사진
        String thumbnailImage = imageNames.get(0);
        itemImageRepository.saveAll(imageNames.stream().map(imageName -> ItemImage.create(itemId, itemType, imageName, Objects.equals(thumbnailImage, imageName))).toList());
    }
}