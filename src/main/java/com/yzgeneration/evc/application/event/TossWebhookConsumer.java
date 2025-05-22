package com.yzgeneration.evc.application.event;

import com.yzgeneration.evc.domain.point.implement.PointChargeProcessor;
import com.yzgeneration.evc.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossWebhookConsumer {

    private final PointChargeProcessor pointChargeProcessor;

    @RabbitListener(queues = "toss.webhook.queue")
    public void receiveTossWebhookEvent(TossPaymentEvent event) {
        try {
            String status = event.getStatus();
            String orderId = event.getOrderId();
            Long memberId = event.getMemberId();
            int point = event.getPrice();
            switch (status) {
                case "DONE": {
                    log.info("TossWebhookConsumer received toss webhook event CHARGE");
                    pointChargeProcessor.charge(orderId, memberId, point);
                }
                case "EXPIRED": {
                    log.info("TossWebhookConsumer received toss webhook event EXPIRED");
                    pointChargeProcessor.cancel(orderId);
                }
                default:
                    log.info("TossWebhookConsumer received toss webhook event {}", status);
            }
        }  catch (Exception e) {
            log.error("[TossWebhookConsumer] toss webhook event error message={}", e.getMessage());
            throw new AmqpRejectAndDontRequeueException(e);
        }

    }

    @RabbitListener(queues = "toss.webhook.dead.letter.queue")
    public void receiveFailedTossWebhookEvent(TossPaymentEvent event) {

    }
}
