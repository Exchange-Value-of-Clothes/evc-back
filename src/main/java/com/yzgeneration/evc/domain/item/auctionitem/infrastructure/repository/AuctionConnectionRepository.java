package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

public interface AuctionConnectionRepository {
    void connect(Long auctionRoomId, Long memberId);

    void disconnect(Long auctionRoomId, Long memberId);
}
