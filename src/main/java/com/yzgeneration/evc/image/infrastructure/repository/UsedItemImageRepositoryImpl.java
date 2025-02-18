package com.yzgeneration.evc.image.infrastructure.repository;

import com.yzgeneration.evc.image.infrastructure.entity.UsedItemImageEntity;
import com.yzgeneration.evc.image.model.UsedItemImage;
import com.yzgeneration.evc.image.service.port.UsedItemImageRepository;
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
