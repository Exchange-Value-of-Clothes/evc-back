package com.yzgeneration.evc.domain.image.service.port;

import com.yzgeneration.evc.domain.image.model.ItemImage;
import com.yzgeneration.evc.domain.item.enums.ItemType;

import java.util.List;

public interface ItemImageRepository {

    ItemImage save(ItemImage itemImage);

    void saveAll(List<ItemImage> itemImages);

    ItemImage findByImageName(String imageName);

    List<String> findImageNamesByItemIdAndItemType(Long itemId, ItemType itemType);

    String findThumbNailNameByItemIdAndItemType(Long itemId, ItemType itemType);

    void deleteAllByItemIdAndItemType(Long itemId, ItemType itemType);

    void deleteByImageNames(Long itemId, ItemType itemType, List<String> imageNames);

    ItemImage findThumbnailByItemIdAndItemType(Long itemId, ItemType itemType);
}