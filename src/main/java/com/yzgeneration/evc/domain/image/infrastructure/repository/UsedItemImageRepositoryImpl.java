package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.yzgeneration.evc.domain.image.infrastructure.entity.UsedItemImageEntity;
import com.yzgeneration.evc.domain.image.model.UsedItemImage;
import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UsedItemImageRepositoryImpl implements UsedItemImageRepository {
    private final UsedItemImageJpaRepository usedItemImageJPARepository;

    @Override
    public UsedItemImage save(UsedItemImage usedItemImage) {
        return usedItemImageJPARepository.save(UsedItemImageEntity.from(usedItemImage)).toModel();
    }

    @Override
    public List<String> findImageUrlsById(Long usedItemId) {
        return usedItemImageJPARepository.findAllImageURLById(usedItemId);
    }
}
