package com.yzgeneration.evc.domain.item.useditem.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import com.yzgeneration.evc.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    public SliceResponse<GetUsedItemsResponse> getUsedItems(@RequestParam(value = "cursor", required = false) LocalDateTime cursor) {
        return usedItemService.getUsedItems(cursor);
    }

    @GetMapping("/{usedItemId}")
    public GetUsedItemResponse getUsedItems(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long usedItemId) {
        return usedItemService.getUsedItem(memberPrincipal.getId(), usedItemId);
    }

    @GetMapping("/search")
    public SliceResponse<GetUsedItemsResponse> searchUsedItems(@RequestParam String q, @RequestParam(value = "cursor", required = false) LocalDateTime cursor) {
        return usedItemService.searchUsedItems(q, cursor);
    }

    @PatchMapping("/{usedItemId}")
    public CommonResponse updateTransactionStatus(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long usedItemId, @Valid @RequestParam TransactionStatus transactionStatus) {
        usedItemService.updateTransactionStatus(memberPrincipal.getId(), usedItemId, transactionStatus);
        return CommonResponse.success();
    }

    @DeleteMapping("/{usedItemId}")
    public CommonResponse deleteUsedItem(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long usedItemId) {
        usedItemService.deleteUsedItem(memberPrincipal.getId(), usedItemId);
        return CommonResponse.success();
    }
}
