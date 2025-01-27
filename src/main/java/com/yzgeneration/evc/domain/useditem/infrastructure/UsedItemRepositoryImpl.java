package com.yzgeneration.evc.domain.useditem.infrastructure;

import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsedItemRepositoryImpl implements UsedItemRepository {
    private final UsedItemJpaRepository usedItemJpaRepository;

    @Override
    public UsedItem save(UsedItem usedItem) {
        return usedItemJpaRepository.save(UsedItemEntity.from(usedItem)).toModel();
    }
}
