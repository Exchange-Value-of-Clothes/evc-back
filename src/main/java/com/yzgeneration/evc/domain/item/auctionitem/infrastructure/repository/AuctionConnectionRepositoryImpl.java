package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionConnectionRepositoryImpl implements AuctionConnectionRepository {

    private final StringRedisTemplate onlineAuctionItem;
    private static final String AUCTION_ROOM_ID = "auctionRoomId:";

    @Override
    public void connect(Long auctionRoomId, Long memberId) {
        onlineAuctionItem.opsForSet().add(AUCTION_ROOM_ID + auctionRoomId, String.valueOf(memberId));
        System.out.println("AuctionItemConnectionRepositoryImpl.connect");
    }

    @Override
    public void disconnect(Long auctionRoomId, Long memberId) {
        onlineAuctionItem.opsForSet().remove(AUCTION_ROOM_ID + auctionRoomId, String.valueOf(memberId));
    }
}
