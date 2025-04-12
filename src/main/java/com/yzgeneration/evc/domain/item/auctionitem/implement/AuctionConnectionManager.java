package com.yzgeneration.evc.domain.item.auctionitem.implement;

import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository.AuctionConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionConnectionManager {
    private final AuctionConnectionRepository auctionConnectionRepository;

    public void connectToAuction(Long auctionRoomId, Long memberId) {
        auctionConnectionRepository.connect(auctionRoomId, memberId);
    }

    public void disconnectToAuction(Long auctionRoomId, Long memberId) {
        auctionConnectionRepository.disconnect(auctionRoomId, memberId);
    }
}
