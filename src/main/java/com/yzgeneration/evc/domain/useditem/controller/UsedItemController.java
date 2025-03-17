package com.yzgeneration.evc.domain.useditem.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsResponse;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/useditems")
@RequiredArgsConstructor
public class UsedItemController {
    private final UsedItemService usedItemService;

    @PostMapping
    public CommonResponse createUsedItem(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @Valid @RequestBody CreateUsedItemRequest createUsedItemRequest) {
        usedItemService.createUsedItem(memberPrincipal.getId(), createUsedItemRequest);
        return CommonResponse.success();
    }

    @GetMapping
    public LoadUsedItemsResponse getUsedItems(@RequestParam int page) {
        return usedItemService.loadUsedItems(page);
    }

    @GetMapping("/{usedItemId}")
    public LoadUsedItemResponse getUsedItems(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long usedItemId) {
        return usedItemService.loadUsedItem(memberPrincipal.getId(), usedItemId);
    }

}
