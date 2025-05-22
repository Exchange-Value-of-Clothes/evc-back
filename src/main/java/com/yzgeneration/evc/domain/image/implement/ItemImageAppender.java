package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.model.ItemImage;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemImageAppender {
    private final ItemImageRepository itemImageRepository;

    public void createItemImages(Long itemId, ItemType itemType, List<String> imageNames, String thumbnailImage) {
        itemImageRepository.saveAll(imageNames.stream().map(imageName -> ItemImage.create(itemId, itemType, imageName)).toList());
        //썸네일 이미지 설정
        ItemImage itemImage = itemImageRepository.findByImageName(thumbnailImage);
        itemImage.toggleIsThumbnail();
        itemImageRepository.save(itemImage);
    }
}