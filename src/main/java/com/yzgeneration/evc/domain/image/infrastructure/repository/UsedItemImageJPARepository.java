package com.yzgeneration.evc.domain.image.infrastructure.repository;

import com.yzgeneration.evc.domain.image.infrastructure.entity.UsedItemImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsedItemImageJPARepository extends JpaRepository<UsedItemImageEntity, Long> {
}
