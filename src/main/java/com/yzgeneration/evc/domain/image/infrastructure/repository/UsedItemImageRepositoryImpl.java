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
    public void saveAll(List<UsedItemImage> usedItemImages) {
        usedItemImageJPARepository.saveAll(usedItemImages.stream().map(UsedItemImageEntity::from).toList());
    }

    @Override
    public List<String> findImageURLsByUsedItemId(Long usedItemId) {
        return usedItemImageJPARepository.findImageURLsByUsedItemId(usedItemId);
    }
}
