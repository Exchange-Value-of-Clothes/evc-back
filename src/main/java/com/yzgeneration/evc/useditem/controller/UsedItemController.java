package com.yzgeneration.evc.useditem.controller;

import com.yzgeneration.evc.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.useditem.dto.UsedItemResponse;
import com.yzgeneration.evc.useditem.service.UsedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("api/items/useditem")
@RequiredArgsConstructor
public class UsedItemController {
    private final UsedItemService usedItemService;

    @PostMapping
    public ResponseEntity<UsedItemResponse> createUsedItem(@RequestPart CreateUsedItem createUsedItem, @RequestPart List<MultipartFile> imageFiles) {
        return new ResponseEntity<>(usedItemService.createUsedITem(createUsedItem, imageFiles), CREATED);
    }
}
