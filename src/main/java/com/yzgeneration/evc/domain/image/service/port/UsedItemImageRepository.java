package com.yzgeneration.evc.domain.image.service.port;

import com.yzgeneration.evc.domain.image.model.UsedItemImage;

import java.util.List;

public interface UsedItemImageRepository {
    UsedItemImage save(UsedItemImage usedItemImage);

    List<String> findUsedItemImagesById(Long usedItemId);
}