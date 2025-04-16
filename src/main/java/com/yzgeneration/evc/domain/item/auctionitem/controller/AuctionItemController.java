package com.yzgeneration.evc.domain.item.auctionitem.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.GetAuctionItemListResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auctionitems")
@RequiredArgsConstructor
public class AuctionItemController {
    private final AuctionItemService auctionItemService;

    @PostMapping
    public CommonResponse createAuctionItem(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody CreateAuctionItemRequest createAuctionItemRequest) {
        auctionItemService.createAuctionItem(memberPrincipal.getId(), createAuctionItemRequest);
        return CommonResponse.success();
    }

    @GetMapping
    public SliceResponse<GetAuctionItemListResponse> getAuctionItems(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestParam(value = "cursor", required = false) LocalDateTime cursor) {
        return auctionItemService.getAuctionItems(memberPrincipal.getId(), cursor);
    }

    @GetMapping("/{auctionItemId}")
    public GetAuctionItemResponse getAuctionItem(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long auctionItemId) {
        return auctionItemService.getAuctionItem(memberPrincipal.getId(), auctionItemId);
    }

    @GetMapping("/search")
    public SliceResponse<GetAuctionItemListResponse> searchAuctionItems(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestParam String keyword, @RequestParam(value = "cursor", required = false) LocalDateTime cursor) {
        return auctionItemService.searchAuctionItems(keyword, memberPrincipal.getId(), cursor);
    }
}
