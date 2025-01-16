package com.yzgeneration.evc.image.infrastructure.repository;

import com.yzgeneration.evc.image.infrastructure.entity.UsedItemImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsedItemImageJPARepository extends JpaRepository<UsedItemImageEntity, Long> {
}
