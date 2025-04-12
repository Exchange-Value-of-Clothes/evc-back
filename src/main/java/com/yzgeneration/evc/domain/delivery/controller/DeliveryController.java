package com.yzgeneration.evc.domain.delivery.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreateRequest;

import com.yzgeneration.evc.domain.delivery.service.DeliveryService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public CommonResponse create(@RequestBody DeliveryCreateRequest request, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        deliveryService.createOrder(request, memberPrincipal.getId());
        return CommonResponse.success();
    }
}
