package com.yzgeneration.evc.domain.chat.controller;

import com.yzgeneration.evc.domain.chat.dto.Chatting;
import com.yzgeneration.evc.domain.chat.implement.ChatConnectionManager;
import com.yzgeneration.evc.domain.chat.service.ChatService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import static com.yzgeneration.evc.common.SessionConstant.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatConnectionManager chatConnectionManager;

    /*
     * 1. ws://localhost:8080/connection "websoket과 연결"
     * 2. /topic/room.{roomId} "채팅방 구독"
     * 3. /pub/chat.message "메시지전송"
     * 4. {"chatRoomId": 1, "content" : "hi"}
     * 5. {"Authorization" : "1"}
     * */
    @PostMapping("/v0")
    public void create(@RequestParam("ownerId") Long ownerId, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        chatService.save(ownerId, memberPrincipal.getId());
    }

    @MessageMapping("chat.message") // SimpHeaderAccessor
    public void sendMessage(StompHeaderAccessor accessor, Chatting chatting) {
        chatService.send(accessor, chatting);
    }

    @EventListener
    public void webSocketSubscribe(SessionSubscribeEvent event) { // https://hong-good.tistory.com/8
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Long chatRoomId = (Long) accessor.getSessionAttributes().get(CHAT_ROOM_KEY);
        Long memberId = (Long) accessor.getSessionAttributes().get(MEMBER_KEY);
        chatConnectionManager.enterChatRoom(chatRoomId, memberId);
    }

    @EventListener
    public void websocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        chatService.disconnect(accessor);
    }

    // TODO 채팅방 탈퇴, 모든 인원이 다 나가면 채팅방 삭제

}
