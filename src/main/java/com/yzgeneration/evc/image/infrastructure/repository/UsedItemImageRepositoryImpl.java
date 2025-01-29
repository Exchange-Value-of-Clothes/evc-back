package com.yzgeneration.evc.image.infrastructure.repository;

import com.yzgeneration.evc.image.infrastructure.entity.UsedItemImageEntity;
import com.yzgeneration.evc.image.model.UsedItemImage;
import com.yzgeneration.evc.image.service.port.UsedItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsedItemImageRepositoryImpl implements UsedItemImageRepository {
    private final UsedItemImageJpaRepository usedItemImageJPARepository;

    @Override
    public UsedItemImage save(UsedItemImage usedItemImage) {
        return usedItemImageJPARepository.save(UsedItemImageEntity.from(usedItemImage)).toModel();
    }
}
