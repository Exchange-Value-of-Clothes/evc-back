package com.yzgeneration.evc.domain.chat.implement;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;


@Component
public class SessionAttributeAccessor {

    public void updateSession(StompHeaderAccessor accessor, String sessionId, Long id) {
        accessor.getSessionAttributes().put(sessionId, id);
    }

    public Long getById(StompHeaderAccessor accessor, String sessionId) {
        return (Long) accessor.getSessionAttributes().get(sessionId);
    }
}
