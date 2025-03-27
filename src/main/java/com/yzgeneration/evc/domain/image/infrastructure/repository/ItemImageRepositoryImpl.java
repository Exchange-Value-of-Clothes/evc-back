package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.infrastructure.entity.ItemImageEntity;
import com.yzgeneration.evc.domain.image.model.ItemImage;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemImageRepositoryImpl implements ItemImageRepository {
    private final ItemImageJpaRepository itemImageJPARepository;

    @Override
    public void saveAll(List<ItemImage> itemImages) {
        itemImageJPARepository.saveAll(itemImages.stream().map(ItemImageEntity::from).toList());
    }

    @Override
    public List<String> findImageNamesByItemIdAndItemType(Long itemId, ItemType itemType) {
        return itemImageJPARepository.findImageURLsByUsedItemId(itemId, itemType);
    }
}
