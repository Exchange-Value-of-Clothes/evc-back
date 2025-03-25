package com.yzgeneration.evc.domain.item.useditem.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemsResponse;
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
    public CommonResponse createUsedItem(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @Valid @RequestBody UsedItemRequest.CreateUsedItemRequest createUsedItemRequest) {
        usedItemService.createUsedItem(memberPrincipal.getId(), createUsedItemRequest);
        return CommonResponse.success();
    }

    @GetMapping
    public GetUsedItemsResponse getUsedItems(@RequestParam int page) {
        return usedItemService.loadUsedItems(page);
    }

    @GetMapping("/{usedItemId}")
    public GetUsedItemResponse getUsedItems(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long usedItemId) {
        return usedItemService.loadUsedItem(memberPrincipal.getId(), usedItemId);
    }

}
