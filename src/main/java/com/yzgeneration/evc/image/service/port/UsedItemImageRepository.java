package com.yzgeneration.evc.image.service.port;

import com.yzgeneration.evc.image.model.UsedItemImage;

import java.util.List;

public interface UsedItemImageRepository {
    UsedItemImage save(UsedItemImage usedItemImage);

    List<String> findImageUrlsById(Long usedItemId);
}
