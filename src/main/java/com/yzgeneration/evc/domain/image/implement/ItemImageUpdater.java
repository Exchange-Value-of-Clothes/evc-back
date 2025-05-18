package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemImageUpdater {
    private final ItemImageRepository itemImageRepository;
    private final ItemImageAppender itemImageAppender;

    public void updateItemImage(Long itemId, ItemType itemType, List<String> imageNames) {
        itemImageRepository.deleteAllByItemIdAndItemType(itemId, itemType);
        itemImageAppender.createItemImages(itemId, itemType, imageNames);
    }
}
