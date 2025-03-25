package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.infrastructure.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {

    @Query("""
                SELECT i.imageName
                From ImageEntity i
                WHERE i.itemId = :itemId
                AND i.itemType = :itemType
                AND i.isThumbnail = true
            """)
    String findThumbnailByUsedItemId(Long itemId, ItemType itemType);

    @Query("""
            SELECT i.imageName
            FROM ImageEntity i
            WHERE i.itemId = :itemId
            AND i.itemType = :itemType
            """)
    List<String> findImageURLsByUsedItemId(Long itemId, ItemType itemType);
}