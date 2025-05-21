package com.yzgeneration.evc.domain.item.auctionitem.implement;

import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionBidResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionBidToListener;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionBidListener {
    private final AuctionItemRepository auctionItemRepository;
    private final AuctionRoomRepository auctionRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "auction.queue")
    public void receiveAuctionBidMessage(AuctionBidToListener auctionBidToListener) {
        try {
            Long auctionItemId = auctionRoomRepository.findAuctionItemIdById(auctionBidToListener.getAuctionRoomId());
            messagingTemplate.convertAndSend("/topic/auction-room." + auctionBidToListener.getAuctionRoomId(),
                    new AuctionBidResponse(auctionBidToListener.getBidderId(), auctionBidToListener.getCurrentPrice(), auctionItemRepository.countParticipantById(auctionItemId)));
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("경매 처리 실패: 재큐 금지", e);
        }
    }
}
