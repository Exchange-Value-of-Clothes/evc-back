package com.yzgeneration.evc.domain.item.auctionitem.service.port;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionRoom;

import java.util.Optional;

public interface AuctionRoomRepository {
    Long save(AuctionRoom auctionRoom);

    Optional<Long> findByAuctionItemId(Long auctionItemId);
}
