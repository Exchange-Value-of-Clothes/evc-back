package com.yzgeneration.evc.domain.item.auctionitem.implement;

import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionBidToListener;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionBidListener {
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "auction.queue")
    public void receiveChatMessage(AuctionBidToListener auctionBidToListener) {
        messagingTemplate.convertAndSend("/topic/room.auction." + auctionBidToListener.getAuctionRoomId(), auctionBidToListener.getCurrentPrice());
    }
}
