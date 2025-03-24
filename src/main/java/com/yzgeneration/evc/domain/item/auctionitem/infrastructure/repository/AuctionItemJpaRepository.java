package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.AuctionItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionItemJpaRepository extends JpaRepository<AuctionItemEntity, Long> {
}
