package com.yzgeneration.evc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@EnableRabbit
@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;


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

    @Bean(name = "auctionQueue")
    public Queue queueForAuction() {
        return new Queue("auction.queue", true);
    }

    @Bean(name = "auctionTopic")
    public TopicExchange topicExchangeForAuction() {
        return new TopicExchange("auction.topic", true, false);
    }

    @Bean
    public Binding bindingForAuction(@Qualifier("auctionQueue") Queue queue,
                                     @Qualifier("auctionTopic") TopicExchange topicExchangeForAuction) {
        return BindingBuilder.bind(queue).to(topicExchangeForAuction).with("auction-room.*");
    }

//    @Bean(name = "tossWebhookQueue")
//    public Queue tossWebhookQueue() {
//        return QueueBuilder.durable("toss.webhook.queue")
//                .deadLetterExchange("toss.webhook.dead.letter.exchange")
////                .autoDelete()
////                .ttl()
//                .build();
//    }

//    @Bean(name = "tossWebhookExchange")
//    public DirectExchange tossWebhookExchange() {
//        return new DirectExchange("toss.webhook.exchange", true, false);
//    }
//
//    @Bean
//    public Binding tossWebhookBinding(@Qualifier("tossWebhookQueue") Queue tossWebhookQueue,
//                                      @Qualifier("tossWebhookExchange") DirectExchange tossWebhookExchange) {
//        return BindingBuilder.bind(tossWebhookQueue).to(tossWebhookExchange).with("payment");
//    }
//
//    @Bean(name = "tossWebhookDeadLetterQueue")
//    public Queue tossWebhookDeadLetterQueue() {
//        return new Queue("toss.webhook.dead.letter.queue", true);
//    }
//
//    @Bean(name = "tossWebhookDeadLetterExchange")
//    public DirectExchange tossWebhookDeadLetterExchange() {
//        return new DirectExchange("toss.webhook.dead.letter.exchange", true, false);
//    }
//
//    @Bean
//    public Binding tossWebhookDeadLetterBinding(@Qualifier("tossWebhookDeadLetterQueue") Queue tossWebhookDeadLetterQueue,
//                                                @Qualifier("tossWebhookDeadLetterExchange") DirectExchange tossWebhookDeadLetterExchange) {
//        return BindingBuilder.bind(tossWebhookDeadLetterQueue).to(tossWebhookDeadLetterExchange).with("#");
//    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPort(5672);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setRequestedHeartBeat(30);
        factory.setConnectionTimeout(30000);
        return factory;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }


}
