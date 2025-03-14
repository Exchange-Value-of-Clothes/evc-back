package com.yzgeneration.evc.domain.image.service.port;

import com.yzgeneration.evc.domain.image.model.UsedItemImage;

import java.util.List;

public interface UsedItemImageRepository {

    void saveAll(List<UsedItemImage> usedItemImages);

    List<String> findImageURLsByUsedItemId(Long usedItemId);
}