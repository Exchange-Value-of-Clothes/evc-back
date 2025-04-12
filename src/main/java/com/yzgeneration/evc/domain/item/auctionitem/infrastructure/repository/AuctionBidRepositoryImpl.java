package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.AuctionBidDocument;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionBid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionBidRepositoryImpl implements AuctionBidRepository {
    private final AuctionBidMongoRepository auctionBidMongoRepository;

    @Override
    public void save(Long auctionRoomId, Long bidderId, int price) {
        auctionBidMongoRepository.save(AuctionBidDocument.from(AuctionBid.create(auctionRoomId, bidderId, price)));
    }
}
