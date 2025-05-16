package com.yzgeneration.evc.domain.my.implement;

import com.yzgeneration.evc.domain.image.implement.ItemImageUpdater;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.domain.my.dto.MyAuctionItemUpdateRequest;
import com.yzgeneration.evc.domain.my.dto.MyUsedItemUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyItemUpdater {
    private final UsedItemRepository usedItemRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final ItemImageUpdater itemImageUpdater;


    public void updateUsedItem(Long usedItemId, MyUsedItemUpdateRequest myUsedItemUpdateRequest) {
        UsedItem usedItem = usedItemRepository.getById(usedItemId);
        itemImageUpdater.updateItemImage(usedItem.getId(), ItemType.USEDITEM, myUsedItemUpdateRequest.getImageNames());
        usedItem.update(myUsedItemUpdateRequest);
        usedItemRepository.save(usedItem);
    }

    public void updateAuctionItem(Long auctionItemId, MyAuctionItemUpdateRequest myAuctionItemUpdateRequest) {
        AuctionItem auctionItem = auctionItemRepository.getById(auctionItemId);
        if (auctionItemRepository.countParticipantById(auctionItem.getId()) > 0) {
            itemImageUpdater.updateItemImage(auctionItem.getId(), ItemType.AUCTIONITEM, myAuctionItemUpdateRequest.getImageNames());
            auctionItem.updateIfParticipantExists(myAuctionItemUpdateRequest);
            auctionItemRepository.save(auctionItem);
        } else {
            itemImageUpdater.updateItemImage(auctionItem.getId(), ItemType.AUCTIONITEM, myAuctionItemUpdateRequest.getImageNames());
            auctionItem.update(myAuctionItemUpdateRequest);
            auctionItemRepository.save(auctionItem);
        }
    }
}
