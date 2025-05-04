package com.yzgeneration.evc.domain.item.implement;

import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemCounter {
    private final UsedItemRepository usedItemRepository;
    private final AuctionItemRepository auctionItemRepository;

    public Long countPostItem(Long memberId){
        return usedItemRepository.countUsedItemByMemberId(memberId) + auctionItemRepository.countAuctionItemByMemberId(memberId);
    }

}
