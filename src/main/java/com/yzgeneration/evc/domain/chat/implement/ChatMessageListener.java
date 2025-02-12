package com.yzgeneration.evc.domain.chat.implement;

import com.yzgeneration.evc.domain.chat.dto.ChattingToListener;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final StringRedisTemplate redisTemplate;

    @RabbitListener(queues = "chat.queue" ) // 비동기적으로 큐에서 메시지 소비해옴 , ackMode = "MANUAL" :  ack를 직접 날릴 수 있음
    public void receiveChatMessage(ChattingToListener chatting) {
        boolean chatPartnerExist = chatting.isChatPartnerExist();
        if(chatPartnerExist) {
            messagingTemplate.convertAndSend("/topic/room." + chatting.getChatRoomId(), chatting.getContent());
        } else {
            System.out.println("ChatMessageListener.receiveChatMessage ELSE");
            //fcmService.sendNotification(receiverId, "새로운 메시지", chatting.getContent());
        }
    }

    //기본적으로 @RabbitListener는 자동 ACK 모드를 사용해서,
    //리스너가 메시지를 읽으면 RabbitMQ가 자동으로 메시지를 삭제함.
    //하지만 만약 리스너에서 예외가 발생하면?
    //→ 메시지가 삭제되지 않고 다시 큐로 돌아감 (Redelivery 가능).

    //@RabbitListener(queues = "chat.queue", ackMode = "MANUAL")
    //public void receiveChatMessage(Message message, Channel channel) throws IOException {
    //    try {
    //        String content = new String(message.getBody(), StandardCharsets.UTF_8);
    //        System.out.println("content = " + content);
    //
    //        // 정상 처리 후 ACK 전송 → 메시지가 삭제됨
    //        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    //    } catch (Exception e) {
    //        // 메시지 재전송 (처리 실패한 경우)
    //        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
    //    }
    //}

}


