package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.model.ItemImage;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemImageUpdater {
    private final ItemImageRepository itemImageRepository;
    private final ItemImageAppender itemImageAppender;
    private final ItemImageRemover itemImageRemover;

    @Transactional
    public void updateItemImage(Long itemId, ItemType itemType, List<String> addImageNames, List<String> removeImageNames, String thumbnailImage) {
        //썸네일 취소
        ItemImage itemImage = itemImageRepository.findThumbnailByItemIdAndItemType(itemId, itemType);
        itemImage.toggleIsThumbnail();
        itemImageRepository.save(itemImage);

        itemImageRemover.removeItemImage(itemId, itemType, removeImageNames);
        itemImageAppender.createItemImages(itemId, itemType, addImageNames, thumbnailImage);
    }
}
