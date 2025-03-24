package com.yzgeneration.evc.domain.image.service.port;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.model.Image;

import java.util.List;

public interface ImageRepository {

    void saveAll(List<Image> images);

    List<String> findImageNamesByItemIdAndItemType(Long itemId, ItemType itemType);

    String findThumbnailByItemIdAndItemType(Long itemId, ItemType itemType);
}