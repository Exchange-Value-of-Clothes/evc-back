package com.yzgeneration.evc.domain.chat.implement;


import com.yzgeneration.evc.domain.member.authentication.implement.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.yzgeneration.evc.common.SessionConstant.CHAT_ROOM_KEY;
import static com.yzgeneration.evc.common.SessionConstant.MEMBER_KEY;

@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor { // stomp에서 메시지를 받기 전에 핸들링할 수 있는 인터셉터 인터셉터 한번 거친후 여기 세션을 비운채로 넘어가나봄

    private final TokenProvider tokenProvider;
    private final StompHeaderReader stompHeaderReader;
    private final ChatConnectionManager chatConnectionManager;
    private final SessionAttributeAccessor sessionAttributeAccessor;

    // TODO https://shout-to-my-mae.tistory.com/434 예외처리하기
    // @SendToUser
    // HandShakeInterceptor
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) { // JwtFilter는 스프링 시큐리티 필터체인에서 동작하는 서블릿 필터다. 따라서 http 요청에만 동작함. (요청이 dispatcherServlet에 도달하기 전에 필터체인 통과)
        System.out.println("message = " + message);
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = parseToken(stompHeaderReader.getToken(accessor));
            Long memberId = tokenProvider.getMemberId(token);
            Long chatRoomId = Long.valueOf(stompHeaderReader.getChatRoomIdAtNativeHeader(accessor));
            System.out.println("Interceptor memberId = " + memberId);
            System.out.println("Interceptor chatRoomId = " + chatRoomId);
            chatConnectionManager.enterChatRoom(chatRoomId, memberId);
            sessionAttributeAccessor.updateSession(accessor, MEMBER_KEY, memberId);
            sessionAttributeAccessor.updateSession(accessor, CHAT_ROOM_KEY, chatRoomId);
        }

        return message;
    }

    private String parseToken(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return authorization;
    }
}
