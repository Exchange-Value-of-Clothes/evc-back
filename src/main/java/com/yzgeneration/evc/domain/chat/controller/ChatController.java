package com.yzgeneration.evc.domain.chat.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.dto.Chatting;
import com.yzgeneration.evc.domain.chat.implement.ChatConnectionManager;
import com.yzgeneration.evc.domain.chat.service.ChatService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;

import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Slice;
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

    // TODO 채팅방 생성은 중고상품 등록할 때
    @PostMapping
    public CommonResponse createChatRoom(@RequestParam("usedItemId") Long usedItemId, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        chatService.createChatRoomAndChatMember(usedItemId, memberPrincipal.getId());
        return CommonResponse.success();
    }

    @PostMapping("/{chatRoomId}")
    public CommonResponse enterChatRoom(@PathVariable Long chatRoomId, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        chatService.enterChatRoom(chatRoomId, memberPrincipal.getId());
        return CommonResponse.success();
    }

    @GetMapping
    public SliceResponse<ChatRoomListResponse> getChatRooms(@AuthenticationPrincipal MemberPrincipal memberPrincipal, LocalDateTime cursor) {
        return chatService.getChatRooms(memberPrincipal.getId(), cursor);
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
