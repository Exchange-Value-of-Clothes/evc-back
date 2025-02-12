package com.yzgeneration.evc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig { //TODO 빈 등록 과정 보기 (따로 등록한 om를 가져다 쓰는지)

    @Bean
    public Queue queue() { // TODO 메시지 유실 보장하기
        return new Queue("chat.queue", false); // durable : mq가 재실행되어도 큐가 소멸되지 않음
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("chat.topic", false, true); // autoDelete : 마지막 구독자(consumer)가 구독을 취소한 후 큐나 교환기를 자동으로 삭제할지 여부를 결정합니다.
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with("room.*");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }

    @Bean
    public MessageConverter messageConverter(Jackson2JsonMessageConverter converter) { // amqp 패키지의 메시지 컨버터임
        return converter;
    }
}
