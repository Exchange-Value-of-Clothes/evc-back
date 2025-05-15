package com.yzgeneration.evc.domain.image.service.port;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.image.model.ItemImage;

import java.util.List;

public interface ItemImageRepository {

    void saveAll(List<ItemImage> itemImages);

    List<String> findImageNamesByItemIdAndItemType(Long itemId, ItemType itemType);

    String findThumbNailImageNameByItemIdAndItemType(Long itemId, ItemType itemType);

}