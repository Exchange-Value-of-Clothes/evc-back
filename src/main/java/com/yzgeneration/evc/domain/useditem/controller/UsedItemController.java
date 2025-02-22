package com.yzgeneration.evc.domain.useditem.controller;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.CreateUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsResponse;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.validator.EnumValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/useditems")
@RequiredArgsConstructor
public class UsedItemController {
    private final UsedItemService usedItemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CreateUsedItemResponse createUsedItem(@RequestPart CreateUsedItemRequest createUsedItemRequest, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        //토큰으로 하여금 회원 정보 받아오기 추가
        EnumValidator.validate(TransactionType.class, "trasactionType", createUsedItemRequest.getTransactionType());
        EnumValidator.validate(TransactionMode.class, "transactionMode", createUsedItemRequest.getTransactionMode());
        return usedItemService.createUsedItem(createUsedItemRequest, imageFiles);
    }

    @GetMapping
    public LoadUsedItemsResponse getUsedItems(@RequestParam int page) {
        return usedItemService.loadUsedItems(page);
    }

    @GetMapping("/{usedItemId}")
    public LoadUsedItemResponse getUsedItems(@RequestParam Long memberId, @PathVariable Long usedItemId) {
        return usedItemService.loadUsedItem(memberId, usedItemId);
    }
}
