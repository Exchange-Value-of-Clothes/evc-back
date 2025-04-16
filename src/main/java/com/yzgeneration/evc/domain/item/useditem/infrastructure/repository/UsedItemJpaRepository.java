package com.yzgeneration.evc.domain.item.useditem.infrastructure.repository;

import com.yzgeneration.evc.domain.item.useditem.infrastructure.entity.UsedItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UsedItemJpaRepository extends JpaRepository<UsedItemEntity, Long> {
}
