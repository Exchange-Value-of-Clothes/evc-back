package com.yzgeneration.evc.domain.image.implement;

import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemImageRemover {
    private final ItemImageRepository itemImageRepository;
    private final ImageHandler imageHandler;

    public void removeItemImage(Long itemId, ItemType itemType, List<String> itemImages) {
        itemImageRepository.deleteByImageNames(itemId, itemType, itemImages);
        for (String itemImage : itemImages) {
            imageHandler.removeImage(itemImage);
        }
    }
}
