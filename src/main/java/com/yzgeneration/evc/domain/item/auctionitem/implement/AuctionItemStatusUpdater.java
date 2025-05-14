package com.yzgeneration.evc.domain.item.auctionitem.implement;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionItemStatusUpdater {
    private final AuctionItemRepository auctionItemRepository;

    public void updateItemStatus(Long memberId, Long auctionItemId, ItemStatus itemStatus) {
        AuctionItem auctionItem = auctionItemRepository.getById(auctionItemId);
        if (!auctionItem.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.ITEM_ACCESS_DENIED);
        }
        auctionItem.updateItemStatus(itemStatus);
        auctionItemRepository.save(auctionItem);
    }

}
