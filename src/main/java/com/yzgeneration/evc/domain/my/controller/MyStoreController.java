package com.yzgeneration.evc.domain.my.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.MyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.dto.MyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import com.yzgeneration.evc.domain.my.dto.MyAuctionItemUpdateRequest;
import com.yzgeneration.evc.domain.my.dto.MyUsedItemUpdateRequest;
import com.yzgeneration.evc.domain.my.service.MyStoreService;
import com.yzgeneration.evc.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/my/store")
@RequiredArgsConstructor
public class MyStoreController {
    private final MyStoreService myStoreService;
    private final UsedItemService usedItemService;
    private final AuctionItemService auctionItemService;

    @GetMapping("/useditems")
    public MyOrMemberUsedItemsResponse getUsedItems(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestParam(value = "cursor", required = false) LocalDateTime cursor,
                                                    @RequestParam(value = "condition", required = false) TransactionMode condition) {
        return myStoreService.getMyUsedItems(memberPrincipal.getId(), cursor, condition);
    }

    @GetMapping("/auctionitems")
    public MyOrMemberAuctionItemsResponse getAuctionItmes(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestParam(value = "cursor", required = false) LocalDateTime cursor) {
        return myStoreService.getMyAuctionItems(memberPrincipal.getId(), cursor);
    }

    @PatchMapping("/useditems/{usedItemId}")
    public CommonResponse updateTransactionStatus(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long usedItemId, @Valid @RequestParam TransactionStatus transactionStatus) {
        usedItemService.updateTransactionStatus(memberPrincipal.getId(), usedItemId, transactionStatus);
        return CommonResponse.success();
    }

    @DeleteMapping("/useditems/{usedItemId}")
    public CommonResponse deleteUsedItem(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long usedItemId) {
        usedItemService.deleteUsedItem(memberPrincipal.getId(), usedItemId);
        return CommonResponse.success();
    }

    @DeleteMapping("/auctionitems/{auctionItemId}")
    public CommonResponse deleteAuctionItem(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long auctionItemId) {
        auctionItemService.deleteAuctionItem(memberPrincipal.getId(), auctionItemId);
        return CommonResponse.success();
    }

    @PutMapping("/useditems/{usedItemId}")
    public CommonResponse putUsedItem(@PathVariable Long usedItemId, @Valid @RequestBody MyUsedItemUpdateRequest myUsedItemUpdateRequest) {
        myStoreService.putMyUsedItem(usedItemId, myUsedItemUpdateRequest);
        return CommonResponse.success();
    }

    @PutMapping("/auctionitems/{auctionItemId}")
    public CommonResponse putAuctionItem(@PathVariable Long auctionItemId, @Valid @RequestBody MyAuctionItemUpdateRequest myAuctionItemUpdateRequest) {
        myStoreService.putMyAuctionItem(auctionItemId, myAuctionItemUpdateRequest);
        return CommonResponse.success();
    }
}
