package com.yzgeneration.evc.domain.item.auctionitem.service;

import com.yzgeneration.evc.domain.chat.implement.SessionAttributeAccessor;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionBidRequest;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionBidToListener;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionRoomResponse;
import com.yzgeneration.evc.domain.item.auctionitem.implement.AuctionBidProcessor;
import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository.AuctionBidRepository;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import static com.yzgeneration.evc.domain.chat.SessionConstant.AUCTION_ROOM_KEY;
import static com.yzgeneration.evc.domain.chat.SessionConstant.MEMBER_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionBidService {
    private final AuctionBidRepository auctionBidRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final AuctionBidProcessor auctionBidProcessor;
    private final SessionAttributeAccessor sessionAttributeAccessor;
    private final RabbitTemplate rabbitTemplate;

    public AuctionRoomResponse createOrGetAuctionRoom(Long memberId, Long auctionItemId) {
        return new AuctionRoomResponse(auctionBidProcessor.createOrGetAuctionRoom(memberId, auctionItemId));
    }

    public void bidAuctionItem(StompHeaderAccessor accessor, AuctionBidRequest auctionBidRequest) {
        Long auctionRoomId = sessionAttributeAccessor.getById(accessor, AUCTION_ROOM_KEY);
        Long memberId = sessionAttributeAccessor.getById(accessor, MEMBER_KEY);

        auctionBidProcessor.bidAuctionItem(memberId, auctionBidRequest.getAuctionId(), auctionBidRequest.getPoint());
        auctionBidRepository.save(auctionRoomId, memberId, auctionBidRequest.getPoint());
        int currentPrice = auctionItemRepository.getCurrentPriceById(auctionBidRequest.getAuctionId());
        rabbitTemplate.convertAndSend("auction.topic", "auction-room." + auctionRoomId, new AuctionBidToListener(auctionRoomId, memberId, currentPrice));
    }
}
