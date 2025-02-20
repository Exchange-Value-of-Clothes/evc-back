package com.yzgeneration.evc.domain.useditem.controller;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/useditems")
@RequiredArgsConstructor
public class UsedItemController {
    private final UsedItemService usedItemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UsedItemResponse createUsedItem(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @Valid @RequestPart CreateUsedItem createUsedItem, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        //토큰으로 하여금 회원 정보 받아오기 추가
        return usedItemService.createUsedItem(memberPrincipal.getId(), createUsedItem, imageFiles);
    }
//
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
