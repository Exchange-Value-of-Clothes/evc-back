package com.yzgeneration.evc.domain.point.implement;

import com.yzgeneration.evc.application.event.TossPaymentEvent;
import com.yzgeneration.evc.domain.point.enums.PointChargeStatus;
import com.yzgeneration.evc.domain.point.infrastructure.MemberPointRepository;
import com.yzgeneration.evc.domain.point.infrastructure.PointChargeRepository;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import com.yzgeneration.evc.external.pg.PaymentStatusChanged;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PointChargeProcessor {

    private final PointChargeRepository pointChargeRepository;
    private final MemberPointRepository memberPointRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void chargeMemberPointAndPointCharge(PointCharge pointCharge, Long memberId, int point) {
        memberPointRepository.charge(memberId, point);
        pointCharge.confirm();
        pointChargeRepository.save(pointCharge);
    }

    public void sendEvent(PaymentStatusChanged event, Long memberId) {
        rabbitTemplate.convertAndSend("tossWebhookExchange", "payment", TossPaymentEvent.of(event, memberId));
    }

    @Transactional
    public void charge(String orderId, Long memberId, int point) {
        pointChargeRepository.charge(orderId);
        memberPointRepository.charge(memberId, point);
    }

    public void cancel(String orderId) {
        pointChargeRepository.cancel(orderId);
    }

}
