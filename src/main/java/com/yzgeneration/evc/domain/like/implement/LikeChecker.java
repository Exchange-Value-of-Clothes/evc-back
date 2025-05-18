package com.yzgeneration.evc.domain.like.implement;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeChecker {
    private final UsedItemRepository usedItemRepository;
    private final AuctionItemRepository auctionItemRepository;

    public void isSelfLike(Long memberId, Long itemId, ItemType itemType) {
        switch (itemType) {
            case AUCTIONITEM -> {
                AuctionItem auctionItem = auctionItemRepository.getById(itemId);
                if (auctionItem.getMemberId().equals(memberId))
                    throw new CustomException(ErrorCode.SELF_LIKE_NOT_ALLOWED);
            }
            case USEDITEM -> {
                UsedItem usedItem = usedItemRepository.getById(itemId);
                if (usedItem.getMemberId().equals(memberId)) throw new CustomException(ErrorCode.SELF_LIKE_NOT_ALLOWED);
            }
        }
    }


}
