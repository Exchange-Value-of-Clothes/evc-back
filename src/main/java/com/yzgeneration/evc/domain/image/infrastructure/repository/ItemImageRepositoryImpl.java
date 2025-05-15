package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.image.infrastructure.entity.ItemImageEntity;
import com.yzgeneration.evc.domain.image.model.ItemImage;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.yzgeneration.evc.domain.image.infrastructure.entity.QItemImageEntity.itemImageEntity;

@Repository
@RequiredArgsConstructor
public class ItemImageRepositoryImpl implements ItemImageRepository {
    private final ItemImageJpaRepository itemImageJPARepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void saveAll(List<ItemImage> itemImages) {
        itemImageJPARepository.saveAll(itemImages.stream().map(ItemImageEntity::from).toList());
    }

    @Override
    public List<String> findImageNamesByItemIdAndItemType(Long itemId, ItemType itemType) {
        return itemImageJPARepository.findImageURLsByUsedItemId(itemId, itemType);
    }

    @Override
    public String findThumbNailImageNameByItemIdAndItemType(Long itemId, ItemType itemType) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(itemImageEntity)
                        .where(itemImageEntity.itemId.eq(itemId)
                                .and(itemImageEntity.itemType.eq(itemType))
                                .and(itemImageEntity.isThumbnail))
                        .fetchOne())
                .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND))
                .getImageName();
    }
}
