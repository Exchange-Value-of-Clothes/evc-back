package com.yzgeneration.evc.useditem.service.port;

import com.yzgeneration.evc.useditem.model.UsedItem;

import java.util.List;

public interface UsedItemRepository {
    UsedItem save(UsedItem usedItem);

    List<UsedItem> findAll();

    UsedItem findById(Long usedItemId);
}
