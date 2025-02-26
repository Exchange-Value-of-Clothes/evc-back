package com.yzgeneration.evc.config;

import com.yzgeneration.evc.security.StompInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

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
                .setRelayHost(host)
                .setVirtualHost(virtualHost)
                .setRelayPort(61613) // SSL 사용 시 61614, 일반적으로 61613
                .setClientLogin(username)
                .setClientPasscode(password)
                .setSystemLogin(username)
                .setSystemPasscode(password);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompInterceptor);
    }

}
