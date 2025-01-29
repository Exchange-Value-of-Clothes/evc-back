package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.yzgeneration.evc.domain.image.infrastructure.entity.UsedItemImageEntity;
import com.yzgeneration.evc.domain.image.model.UsedItemImage;
import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsedItemImageRepositoryImpl implements UsedItemImageRepository {
    private final UsedItemImageJPARepository usedItemImageJPARepository;

    @Override
    public UsedItemImage save(UsedItemImage usedItemImage) {
        return usedItemImageJPARepository.save(UsedItemImageEntity.from(usedItemImage)).toModel();
    }
}
