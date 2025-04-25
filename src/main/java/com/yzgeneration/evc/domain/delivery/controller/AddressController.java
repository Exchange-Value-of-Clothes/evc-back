package com.yzgeneration.evc.domain.delivery.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.delivery.dto.AddressCreate;
import com.yzgeneration.evc.domain.delivery.dto.SearchCoordinate;
import com.yzgeneration.evc.domain.delivery.dto.SearchCoordinateResponse;
import com.yzgeneration.evc.domain.delivery.service.AddressService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/coordinate")
    public SearchCoordinateResponse searchCoordinate(@RequestBody SearchCoordinate searchCoordinate) {
        return addressService.searchCoordinate(searchCoordinate.getAddressName());
    }

    @PostMapping
    public CommonResponse create(@RequestBody AddressCreate request, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        addressService.create(request.getBasicAddress(), request.getDetailAddress(), request.getLatitude(), request.getLongitude(), memberPrincipal.getId());
        return CommonResponse.success();
    }
}
