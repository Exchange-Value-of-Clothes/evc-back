package com.yzgeneration.evc.domain.useditem.service.port;

import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsedItemRepository {
    UsedItem save(UsedItem usedItem);

    List<UsedItem> findAll();

    UsedItem findById(Long usedItemId);

    Page<UsedItem> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
