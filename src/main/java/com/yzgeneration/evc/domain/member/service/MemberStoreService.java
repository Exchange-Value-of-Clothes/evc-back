package com.yzgeneration.evc.domain.member.service;

import com.yzgeneration.evc.domain.item.auctionitem.dto.MyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.useditem.dto.MyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberStoreService {
    private final UsedItemService usedItemService;
    private final AuctionItemService auctionItemService;

    public MyOrMemberUsedItemsResponse getMemberUsedItems(Long storeMemberId, LocalDateTime cursor, TransactionMode transactionMode) {
        return usedItemService.getMyOrMemberUsedItems(storeMemberId, cursor, transactionMode);
    }

    public MyOrMemberAuctionItemsResponse getMemberAuctionItems(Long storeMemberId, LocalDateTime cursor) {
        return auctionItemService.getMyOrMemberAuctionItems(storeMemberId, cursor);
    }
}
