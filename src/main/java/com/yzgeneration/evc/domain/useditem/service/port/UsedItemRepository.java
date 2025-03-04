package com.yzgeneration.evc.domain.useditem.service.port;

import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UsedItemRepository {
    UsedItem save(UsedItem usedItem);

    String findNicknameByUsedItemId(Long usedItemId);

    Slice<UsedItem> findAll(Pageable pageable);

    UsedItem findById(Long usedItemId);
}