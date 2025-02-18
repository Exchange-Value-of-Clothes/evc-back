package com.yzgeneration.evc.image.infrastructure.repository;

import com.yzgeneration.evc.image.infrastructure.entity.UsedItemImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsedItemImageJpaRepository extends JpaRepository<UsedItemImageEntity, Long> {
    @Query("SELECT ui.imageURL FROM UsedItemImageEntity ui WHERE ui.usedItemId = :usedItemId")
    List<String> findAllImageURLById(Long usedItemId);
}
