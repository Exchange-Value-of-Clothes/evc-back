package com.yzgeneration.evc.domain.my.service;

import com.yzgeneration.evc.domain.item.auctionitem.dto.MyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.useditem.dto.MyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import com.yzgeneration.evc.domain.my.dto.MyAuctionItemUpdateRequest;
import com.yzgeneration.evc.domain.my.dto.MyUsedItemUpdateRequest;
import com.yzgeneration.evc.domain.my.implement.MyItemUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MyStoreService {
    private final UsedItemService usedItemService;
    private final AuctionItemService auctionItemService;
    private final MyItemUpdater myItemUpdater;

    public MyOrMemberUsedItemsResponse getMyUsedItems(Long memberId, LocalDateTime cursor, TransactionMode transactionMode) {
        return usedItemService.getMyOrMemberUsedItems(memberId, cursor, transactionMode);
    }

    public MyOrMemberAuctionItemsResponse getMyAuctionItems(Long memberId, LocalDateTime cursor) {
        return auctionItemService.getMyOrMemberAuctionItems(memberId, cursor);
    }

    public void putMyUsedItem(Long usedItemId, MyUsedItemUpdateRequest myUsedItemUpdateRequest) {
        myItemUpdater.updateUsedItem(usedItemId, myUsedItemUpdateRequest);
    }

    public void putMyAuctionItem(Long auctionItemId, MyAuctionItemUpdateRequest myAuctionItemUpdateRequest) {
        myItemUpdater.updateAuctionItem(auctionItemId, myAuctionItemUpdateRequest);
    }
}
