package com.yzgeneration.evc.domain.useditem.controller;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.validator.EnumValidator;
import jakarta.validation.Valid;
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
    public UsedItemResponse createUsedItem(@RequestPart CreateUsedItem createUsedItem, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        //토큰으로 하여금 회원 정보 받아오기 추가
        EnumValidator.validate(TransactionType.class, "trasactionType", createUsedItem.getCreateTransaction().getTransactionType());
        EnumValidator.validate(TransactionMode.class, "transactionMode", createUsedItem.getCreateTransaction().getTransactionMode());
        return usedItemService.createUsedItem(createUsedItem, imageFiles);
    }
//    @GetMapping
//    public List<UsedItemResponse> getUsedItems(){
//
//    }
//
//    @GetMapping("/{usedItemId}")
//    public List<UsedItemResponse> getUsedItems(@PathVariable Long usedItemId){
//
//    }
}
