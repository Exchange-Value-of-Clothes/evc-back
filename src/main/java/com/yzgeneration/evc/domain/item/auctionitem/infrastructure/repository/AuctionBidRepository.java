package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

public interface AuctionBidRepository {
    void save(Long auctionRoomId, Long bidderId, int price);
}
