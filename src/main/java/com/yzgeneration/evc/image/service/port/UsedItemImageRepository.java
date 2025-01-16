package com.yzgeneration.evc.image.service.port;

import com.yzgeneration.evc.image.model.UsedItemImage;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedItemImageRepository {
    UsedItemImage save(UsedItemImage usedItemImage);
}
