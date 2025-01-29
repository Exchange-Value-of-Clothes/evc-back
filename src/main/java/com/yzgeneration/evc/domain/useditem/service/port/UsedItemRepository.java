package com.yzgeneration.evc.domain.useditem.service.port;

import com.yzgeneration.evc.domain.useditem.model.UsedItem;

public interface UsedItemRepository {
    UsedItem save(UsedItem usedItem);
}
