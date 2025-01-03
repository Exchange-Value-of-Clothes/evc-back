package com.yzgeneration.evc.useditem.infrastructure;

import com.yzgeneration.evc.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsedItemRepositoryImpl implements UsedItemRepository {
    private final UsedItemJpaRepository usedItemJpaRepository;

}
