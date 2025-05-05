package com.yzgeneration.evc.domain.my.controller;

import com.yzgeneration.evc.domain.item.auctionitem.dto.MyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyAuctionItemController {
    private final AuctionItemService auctionItemService;

    @GetMapping("/auctionitems")
    public MyOrMemberAuctionItemsResponse getAuctionItmes(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestParam(value = "cursor", required = false) LocalDateTime cursor) {
        return auctionItemService.getMyOrMemberAuctionItems(memberPrincipal.getId(), cursor);
    }
}
