package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.AuctionRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRoomJpaRepository extends JpaRepository<AuctionRoomEntity, Long> {
}
