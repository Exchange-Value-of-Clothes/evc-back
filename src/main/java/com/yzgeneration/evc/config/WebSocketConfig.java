package com.yzgeneration.evc.config;

import com.yzgeneration.evc.domain.chat.implement.StompInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompInterceptor stompInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connection")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/connection")
                .setAllowedOriginPatterns("*");

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub") // 메세지를 보낼(publish) 경로를 설정
                .setPathMatcher(new AntPathMatcher(".")) // RabbitMQ에서는 기본적으로 queue, exchange의 이름,라우팅 키,패턴 등을 작성할 때 . 을. 구분자로 사용
                .setPreservePublishOrder(true)
                .enableStompBrokerRelay("/topic","/exchange","/queue", "/amq/direct")
                .setRelayHost("localhost") // 다음 아래부터 메시지 브로커를 RabbitMQ와 연결하는 부분
                .setRelayPort(61613) // RabbitMQ STOMP 기본 포트 TODO 5672, 61613, 15672(이건 모니터링용 포트?)차이점
                .setClientLogin("guest")
                .setClientPasscode("guest")
                .setSystemLogin("guest")
                .setSystemPasscode("guest");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompInterceptor);
    }

}
