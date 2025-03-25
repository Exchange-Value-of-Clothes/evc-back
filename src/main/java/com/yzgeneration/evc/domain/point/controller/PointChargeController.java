package com.yzgeneration.evc.domain.point.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.point.dto.PointChargeConfirmRequest;
import com.yzgeneration.evc.domain.point.dto.PointChargeOrderRequest;
import com.yzgeneration.evc.domain.point.dto.PointChargeOrderResponse;
import com.yzgeneration.evc.domain.point.service.PointChargeService;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointChargeController {

    private final PointChargeService pointChargeService;

    // 결제 주문 생성
    @PostMapping
    public PointChargeOrderResponse createOrder(@RequestBody PointChargeOrderRequest pointChargeOrderRequest, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        PointCharge pointCharge = pointChargeService.createOrder(PointCharge.create(memberPrincipal.getId(), pointChargeOrderRequest.getPrice()));
        return new PointChargeOrderResponse(pointCharge.getOrderId(), pointCharge.getPrice());
    }

    // 결제 승인
    @PostMapping("/confirm")
    public CommonResponse confirm(@RequestBody PointChargeConfirmRequest pointChargeConfirmRequest, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        pointChargeService.confirm(pointChargeConfirmRequest.getOrderId(), pointChargeConfirmRequest.getPaymentKey(), pointChargeConfirmRequest.getAmount(), memberPrincipal.getId());
        return CommonResponse.success();
    }

    // 결제 조회


    // 웹훅
//    @PostMapping("/webhook")
//    public void webhook(@RequestBody PaymentStatusChanged paymentStatusChanged) {
//        System.out.println("paymentStatusChanged.toString() = " + paymentStatusChanged.toString());
//    }


}
