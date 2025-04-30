package com.yzgeneration.evc.domain.chat.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.*;
import com.yzgeneration.evc.domain.chat.service.ChatService;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatMessageSliceResponse enter(@RequestBody ChatEnter chatEnter, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return chatService.getChatRoomByTradeRequest(chatEnter.getItemId(), ItemType.valueOf(chatEnter.getItemType()),chatEnter.getOwnerId(), memberPrincipal.getId());
    }

    @GetMapping
    public SliceResponse<ChatRoomListResponse> getChatRooms(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestParam(value = "cursor", required = false) LocalDateTime cursor) {
        return chatService.getChatRooms(memberPrincipal.getId(), cursor);
    }

    @GetMapping("/{chatRoomId}")
    public ChatMessageSliceResponse getChatRoom(@PathVariable("chatRoomId") Long chatRoomId, @RequestParam(value = "cursor", required = false) LocalDateTime cursor,
                                                @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return chatService.getChatRoomByListSelection(chatRoomId, cursor, memberPrincipal.getId());
    }

    @MessageMapping("chat.message") // SimpHeaderAccessor
    public void sendMessage(StompHeaderAccessor accessor, Chatting chatting) {
        chatService.send(accessor, chatting);
    }

    // TODO 모든 인원이 다 나가면 채팅방 및 채팅멤버 삭제
    @PatchMapping("/{chatRoomId}/exit")
    public CommonResponse exit(@PathVariable("chatRoomId") Long chatRoomId , @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        chatService.exit(chatRoomId, memberPrincipal.getId());
        return CommonResponse.success();
    }



}
