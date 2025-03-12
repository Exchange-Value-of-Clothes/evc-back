package com.yzgeneration.evc.domain.chat.controller;

import com.yzgeneration.evc.domain.chat.implement.ChatConnectionManager;
import com.yzgeneration.evc.domain.chat.implement.SessionAttributeAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import static com.yzgeneration.evc.domain.chat.SessionConstant.CHAT_ROOM_KEY;
import static com.yzgeneration.evc.domain.chat.SessionConstant.MEMBER_KEY;

@Component
@RequiredArgsConstructor
public class ChatEventListener {

    private final ChatConnectionManager chatConnectionManager;
    private final SessionAttributeAccessor sessionAttributeAccessor;

    @EventListener
    public void webSocketSubscribe(SessionSubscribeEvent event) { // https://hong-good.tistory.com/8
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Long chatRoomId = (Long) accessor.getSessionAttributes().get(CHAT_ROOM_KEY);
        Long memberId = (Long) accessor.getSessionAttributes().get(MEMBER_KEY);
        chatConnectionManager.connectToChatRoom(chatRoomId, memberId);
    }

    @EventListener
    public void websocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Long chatRoomId = sessionAttributeAccessor.getById(accessor, CHAT_ROOM_KEY);
        Long memberId = sessionAttributeAccessor.getById(accessor, MEMBER_KEY);
        chatConnectionManager.exitChatRoom(chatRoomId, memberId);
    }

}
