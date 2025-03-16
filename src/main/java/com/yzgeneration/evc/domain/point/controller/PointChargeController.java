package com.yzgeneration.evc.domain.point.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.point.dto.PointChargeConfirmRequest;
import com.yzgeneration.evc.domain.point.dto.PointChargeOrderRequest;
import com.yzgeneration.evc.domain.point.dto.PointChargeOrderResponse;
import com.yzgeneration.evc.domain.point.dto.PointChargeVerifyRequest;
import com.yzgeneration.evc.domain.point.service.PointChargeService;
import com.yzgeneration.evc.domain.point.enums.PointChargeType;
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

    @PostMapping
    public PointChargeOrderResponse createOrder(@RequestBody PointChargeOrderRequest pointChargeOrderRequest, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        PointCharge pointCharge = pointChargeService.createOrder(PointCharge.create(memberPrincipal.getId(), PointChargeType.valueOf(pointChargeOrderRequest.getPointChargeType())));
        return new PointChargeOrderResponse(pointCharge.getOrderId(), pointCharge.getPointChargeType().getPrice());
    }

    @PostMapping("/verify")
    public CommonResponse verify(@RequestBody PointChargeVerifyRequest pointChargeVerifyRequest) {
        pointChargeService.verify(pointChargeVerifyRequest);
        return CommonResponse.success();
    }

    @PostMapping("/confirm")
    public void confirm(@RequestBody PointChargeConfirmRequest pointChargeConfirmRequest) {
        pointChargeService.confirm(pointChargeConfirmRequest);
    }
}
