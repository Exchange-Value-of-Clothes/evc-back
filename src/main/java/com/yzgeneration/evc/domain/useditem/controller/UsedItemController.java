package com.yzgeneration.evc.domain.useditem.controller;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.CreateUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsResponse;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/useditems")
@RequiredArgsConstructor
public class UsedItemController {
    private final UsedItemService usedItemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CreateUsedItemResponse createUsedItem(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestPart @Valid CreateUsedItemRequest createUsedItemRequest, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        //토큰으로 하여금 회원 정보 받아오기 추가
        return usedItemService.createUsedItem(memberPrincipal.getId(), createUsedItemRequest, imageFiles);
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
