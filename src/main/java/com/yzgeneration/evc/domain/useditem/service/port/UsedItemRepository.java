package com.yzgeneration.evc.domain.useditem.service.port;

import com.yzgeneration.evc.domain.useditem.model.UsedItem;

import java.util.List;

public interface UsedItemRepository {
    UsedItem save(UsedItem usedItem);

    List<UsedItem> findAll();

    UsedItem findById(Long usedItemId);
}
