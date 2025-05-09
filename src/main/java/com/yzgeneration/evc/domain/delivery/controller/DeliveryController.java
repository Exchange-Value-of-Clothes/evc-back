package com.yzgeneration.evc.domain.delivery.controller;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.delivery.dto.DeliveryPrepare;
import com.yzgeneration.evc.domain.delivery.model.DeliveryView;
import com.yzgeneration.evc.domain.delivery.service.DeliveryService;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.external.delivery.GetKakaoMobilityOrder;
import com.yzgeneration.evc.external.delivery.KakaoMobilityOrderResponse;
import com.yzgeneration.evc.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public KakaoMobilityOrderResponse order(@RequestBody @Valid DeliveryPrepare request, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return deliveryService.order(ItemType.valueOf(request.getItemType()), request.getItemId(), request.getBuyerId(), request.getSellerId(), memberPrincipal.getId());
    }

    @GetMapping("/{orderId}")
    public GetKakaoMobilityOrder get(@PathVariable("orderId") String orderId, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return deliveryService.get(orderId, memberPrincipal.getId());
    }

    @GetMapping
    public SliceResponse<DeliveryView> findList(@RequestParam(value = "cursor", required = false) LocalDateTime cursor, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return deliveryService.findList(cursor, memberPrincipal.getId());
    }

}
