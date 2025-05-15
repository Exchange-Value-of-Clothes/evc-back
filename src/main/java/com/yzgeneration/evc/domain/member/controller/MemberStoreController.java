package com.yzgeneration.evc.domain.member.controller;

import com.yzgeneration.evc.domain.item.auctionitem.dto.MyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.useditem.dto.MyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.member.service.MemberStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberStoreController {
    private final MemberStoreService memberStoreService;

    @GetMapping("/{storeMemberId}/store/useditems")
    public MyOrMemberUsedItemsResponse getMemberUsedItems(@PathVariable Long storeMemberId, @RequestParam(value = "cursor", required = false) LocalDateTime cursor, @RequestParam(value = "condition", required = false) TransactionMode condition) {
        return memberStoreService.getMemberUsedItems(storeMemberId, cursor, condition);
    }

    @GetMapping("/{storeMemberId}/store/auctionitems")
    public MyOrMemberAuctionItemsResponse getMemberAuctionItems(@PathVariable Long storeMemberId, @RequestParam(value = "cursor", required = false) LocalDateTime cursor) {
        return memberStoreService.getMemberAuctionItems(storeMemberId, cursor);
    }
}
