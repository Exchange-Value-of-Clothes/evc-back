package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.infrastructure.entity.ItemImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemImageJpaRepository extends JpaRepository<ItemImageEntity, Long> {

    @Query("""
            SELECT i.imageName
            FROM ItemImageEntity i
            WHERE i.itemId = :itemId
            AND i.itemType = :itemType
            """)
    List<String> findImageURLsByUsedItemId(Long itemId, ItemType itemType);
}