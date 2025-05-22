package com.yzgeneration.evc.domain.point.service;

import com.yzgeneration.evc.domain.point.implement.PointChargeProcessor;
import com.yzgeneration.evc.domain.point.implement.PointChargeValidator;
import com.yzgeneration.evc.domain.point.infrastructure.PointChargeRepository;
import com.yzgeneration.evc.domain.point.model.PointCharge;

import com.yzgeneration.evc.external.pg.Payment;
import com.yzgeneration.evc.external.pg.PaymentGateway;
import com.yzgeneration.evc.external.pg.PaymentStatusChanged;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointChargeService {

    private final PointChargeRepository pointChargeRepository;
    private final PointChargeValidator pointChargeValidator;
    private final PaymentGateway paymentGateway;
    private final PointChargeProcessor pointChargeProcessor;

    public PointCharge createOrder(PointCharge pointCharge) {
        return pointChargeRepository.save(pointCharge);
    }

    public void confirm(String orderId, String paymentKey, int amount, Long memberId) {
        pointChargeValidator.validate(orderId, amount);
        Payment payment = paymentGateway.confirm(orderId, paymentKey, amount);
        PointCharge pointCharge = pointChargeRepository.getById(orderId);
        pointChargeProcessor.chargeMemberPointAndPointCharge(pointCharge, memberId, payment.getTotalAmount());
    }

    public void confirmWithWebhook(String orderId, String paymentKey, int amount, Long id) {
        pointChargeValidator.validate(orderId, amount);
        paymentGateway.confirmWithWebhook(orderId, paymentKey, amount);
    }

    public void consumeWebhook(PaymentStatusChanged paymentStatusChanged, Long memberId) {
        if (paymentStatusChanged.getEventType().equals("PAYMENT_STATUS_CHANGED")) pointChargeProcessor.sendEvent(paymentStatusChanged, memberId);
    }
}
