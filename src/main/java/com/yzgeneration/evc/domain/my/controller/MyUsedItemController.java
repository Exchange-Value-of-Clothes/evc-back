package com.yzgeneration.evc.domain.my.controller;

import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.useditem.dto.MyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyUsedItemController {
    private final UsedItemService usedItemService;

    @GetMapping("/useditems")
    public MyOrMemberUsedItemsResponse getUsedItems(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestParam(value = "cursor", required = false) LocalDateTime cursor,
                                                    @RequestParam(value = "condition", required = false) TransactionMode condition) {
        return usedItemService.getMyOrMemberUsedItems(memberPrincipal.getId(), cursor, condition);
    }

    //TODO 내 상품 상태 변경
}
