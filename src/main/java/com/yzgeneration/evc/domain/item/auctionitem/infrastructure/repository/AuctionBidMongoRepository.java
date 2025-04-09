package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.AuctionBidDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuctionBidMongoRepository extends MongoRepository<AuctionBidDocument, Long> {
}
