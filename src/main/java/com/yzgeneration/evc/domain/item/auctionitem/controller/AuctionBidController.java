package com.yzgeneration.evc.domain.item.auctionitem.controller;

import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionBidRequest;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionRoomResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionBidService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bid")
@RequiredArgsConstructor
public class AuctionBidController {
    private final AuctionBidService auctionBidService;

    @PostMapping("/{auctionItemId}")
    public AuctionRoomResponse createOrGetAuctionRoom(@PathVariable Long auctionItemId) {
        return auctionBidService.createOrGetAuctionRoom(auctionItemId);
    }

    @MessageMapping("auction.message")
    public void bidAuctionItem(StompHeaderAccessor accessor, AuctionBidRequest auctionBidRequest) {
        auctionBidService.bidAuctionItem(accessor, auctionBidRequest);
    }
}
