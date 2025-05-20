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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyItemUpdater {
    private final UsedItemRepository usedItemRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final ItemImageUpdater itemImageUpdater;


    public void updateUsedItem(Long usedItemId, MyUsedItemUpdateRequest myUsedItemUpdateRequest) {
        UsedItem usedItem = usedItemRepository.getById(usedItemId);
        if (myUsedItemUpdateRequest.getAddImageNames().isEmpty() && myUsedItemUpdateRequest.getRemoveImageNames().isEmpty()) {
            // 변경할 이미지가 없으므로 처리하지 않음
        }
        log.info("updateUsedItem : {}", usedItem.getItemDetails().getTitle());
        itemImageUpdater.updateItemImage(usedItem.getId(), ItemType.USEDITEM, myUsedItemUpdateRequest.getAddImageNames(), myUsedItemUpdateRequest.getRemoveImageNames(), myUsedItemUpdateRequest.getThumbnailImage());
        usedItem.update(myUsedItemUpdateRequest);
        usedItemRepository.save(usedItem);
    }

    public void updateAuctionItem(Long auctionItemId, MyAuctionItemUpdateRequest myAuctionItemUpdateRequest) {
        AuctionItem auctionItem = auctionItemRepository.getById(auctionItemId);
        if (myAuctionItemUpdateRequest.getAddImageNames().isEmpty() && myAuctionItemUpdateRequest.getRemoveImageNames().isEmpty()) {
            // 변경할 이미지가 없으므로 처리하지 않음
        }

        if (auctionItemRepository.countParticipantById(auctionItem.getId()) > 0) { //참여자가 있으면 시가 변경 X
            itemImageUpdater.updateItemImage(auctionItem.getId(), ItemType.AUCTIONITEM, myAuctionItemUpdateRequest.getAddImageNames(), myAuctionItemUpdateRequest.getRemoveImageNames(), myAuctionItemUpdateRequest.getThumbnailImage());
            auctionItem.updateIfParticipantExists(myAuctionItemUpdateRequest);
            auctionItemRepository.save(auctionItem);
        } else {
            itemImageUpdater.updateItemImage(auctionItem.getId(), ItemType.AUCTIONITEM, myAuctionItemUpdateRequest.getAddImageNames(), myAuctionItemUpdateRequest.getRemoveImageNames(), myAuctionItemUpdateRequest.getThumbnailImage());
            auctionItem.update(myAuctionItemUpdateRequest);
            auctionItemRepository.save(auctionItem);
        }
    }
}
