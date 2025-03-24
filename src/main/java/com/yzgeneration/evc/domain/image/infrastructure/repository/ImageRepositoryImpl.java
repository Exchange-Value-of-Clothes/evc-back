package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.infrastructure.entity.ImageEntity;
import com.yzgeneration.evc.domain.image.model.Image;
import com.yzgeneration.evc.domain.image.service.port.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {
    private final ImageJpaRepository imageJPARepository;

    @Override
    public void saveAll(List<Image> images) {
        imageJPARepository.saveAll(images.stream().map(ImageEntity::from).toList());
    }

    @Override
    public List<String> findImageNamesByItemIdAndItemType(Long itemId, ItemType itemType) {
        return imageJPARepository.findImageURLsByUsedItemId(itemId, itemType);
    }

    @Override
    public String findThumbnailByItemIdAndItemType(Long itemId, ItemType itemType) {
        return imageJPARepository.findThumbnailByUsedItemId(itemId, itemType);
    }
}
