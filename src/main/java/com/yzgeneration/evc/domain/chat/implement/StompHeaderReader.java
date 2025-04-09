package com.yzgeneration.evc.domain.chat.implement;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import static com.yzgeneration.evc.domain.chat.SessionConstant.*;


@Component
public class StompHeaderReader {

    public String getChatRoomIdAtNativeHeader(StompHeaderAccessor accessor) {
        String chatRoomId = accessor.getFirstNativeHeader(CHAT_ROOM_KEY);
        if (chatRoomId == null) {
            throw new RuntimeException("ChatRoom ID not found in header");
        }
        return chatRoomId;
    }

    public String getAuctionRoomIdAtNativeHeader(StompHeaderAccessor accessor) {
        String auctionRoomId = accessor.getFirstNativeHeader(AUCTION_ROOM_KEY);
        if (auctionRoomId == null) {
            throw new RuntimeException("AuctionRoom ID not found in header");
        }
        return auctionRoomId;
    }

    public String getToken(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader(AUTHORIZATION);
        if (token == null) {
            throw new RuntimeException("Token not found in header");
        }
        return token;
    }

}
