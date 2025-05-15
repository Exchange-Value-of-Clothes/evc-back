package com.yzgeneration.evc.domain.like.infrastructure.repository;

import com.yzgeneration.evc.domain.like.infrastructure.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepositoy extends JpaRepository<LikeEntity, Long> {
}
