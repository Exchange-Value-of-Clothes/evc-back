package com.yzgeneration.evc.application.event;

import com.yzgeneration.evc.domain.chat.implement.SessionAttributeAccessor;
import com.yzgeneration.evc.domain.item.auctionitem.implement.AuctionConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import static com.yzgeneration.evc.domain.chat.SessionConstant.AUCTION_ROOM_KEY;
import static com.yzgeneration.evc.domain.chat.SessionConstant.MEMBER_KEY;

@Component
@RequiredArgsConstructor
public class AuctionBidEventListener {
    private final AuctionConnectionManager auctionConnectionManager;
    private final SessionAttributeAccessor sessionAttributeAccessor;

    @EventListener
    public void webSocketSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();

        if (destination.startsWith("/topic/auction-room")) { //이벤트에 대한 경로를 설정함으로써 필요한 key만 redis에 들어감
            Long auctionRoomId = sessionAttributeAccessor.getById(accessor, AUCTION_ROOM_KEY);
            Long memberId = sessionAttributeAccessor.getById(accessor, MEMBER_KEY);
            auctionConnectionManager.connectToAuction(auctionRoomId, memberId);
        }
    }

    @EventListener
    public void websocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (sessionAttributeAccessor.getById(accessor, AUCTION_ROOM_KEY) != null) {
            Long chatRoomId = sessionAttributeAccessor.getById(accessor, AUCTION_ROOM_KEY);
            Long memberId = sessionAttributeAccessor.getById(accessor, MEMBER_KEY);
            auctionConnectionManager.disconnectToAuction(chatRoomId, memberId);
        }
    }
}
