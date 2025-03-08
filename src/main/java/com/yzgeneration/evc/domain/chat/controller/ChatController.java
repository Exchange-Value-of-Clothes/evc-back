package com.yzgeneration.evc.domain.chat.controller;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.*;
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

import java.time.LocalDateTime;

import static com.yzgeneration.evc.domain.chat.SessionConstant.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatConnectionManager chatConnectionManager;

    @PostMapping
    public ChatMessageSliceResponse enter(@RequestBody ChatEnter chatEnter, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return chatService.getChatRoomByTradeRequest(chatEnter.getUsedItemId(), chatEnter.getOwnerId(), memberPrincipal.getId());
    }

    @GetMapping
    public SliceResponse<ChatRoomListResponse> getChatRooms(@AuthenticationPrincipal MemberPrincipal memberPrincipal, LocalDateTime cursor) {
        return chatService.getChatRooms(memberPrincipal.getId(), cursor);
    }

    @GetMapping("/{chatRoomId}")
    public ChatMessageSliceResponse getChatRoom(@PathVariable("chatRoomId") Long chatRoomId, @RequestParam(value = "cursor", required = false) LocalDateTime cursor) {
        return chatService.getChatRoomByListSelection(chatRoomId, cursor);
    }

    @MessageMapping("chat.message") // SimpHeaderAccessor
    public void sendMessage(StompHeaderAccessor accessor, Chatting chatting) {
        chatService.send(accessor, chatting);
    }

    @EventListener // TODO  ChatListener 분리
    public void webSocketSubscribe(SessionSubscribeEvent event) { // https://hong-good.tistory.com/8
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Long chatRoomId = (Long) accessor.getSessionAttributes().get(CHAT_ROOM_KEY);
        Long memberId = (Long) accessor.getSessionAttributes().get(MEMBER_KEY);
        chatConnectionManager.connectToChatRoom(chatRoomId, memberId);
    }

    @EventListener
    public void websocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        chatService.disconnect(accessor);
    }


    // TODO 채팅방 탈퇴, 모든 인원이 다 나가면 채팅방 삭제

}
