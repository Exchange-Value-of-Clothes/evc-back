package com.yzgeneration.evc.domain.item.auctionitem.implement;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionRoom;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionRoomRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuctionBidProcessor {
    private final AuctionItemRepository auctionItemRepository;
    private final AuctionRoomRepository auctionRoomRepository;

    public Long createOrGetAuctionRoom(Long auctionItemId) {
        return auctionRoomRepository.findByAuctionItemId(auctionItemId)
                .orElseGet(() -> auctionRoomRepository.save(AuctionRoom.create(auctionItemId)));
    }

    @Transactional
    public void bidAuctionItem(Long memberId, Long auctionItemId, int point) {
        if (!auctionItemRepository.canMemberBidByIdAndMemberId(auctionItemId, memberId)) {
            throw new CustomException(ErrorCode.SELF_BID_NOT_ALLOWED);
        }
        if (!auctionItemRepository.checkMemberPointById(auctionItemId, memberId, point)) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_POINT);
        }
        auctionItemRepository.updateCurrentPrice(auctionItemId, point);
    }
}
