package com.yzgeneration.evc.domain.useditem.infrastructure.repository;

import com.yzgeneration.evc.domain.useditem.infrastructure.entity.UsedItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsedItemJpaRepository extends JpaRepository<UsedItemEntity, Long> {
    Page<UsedItemEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
