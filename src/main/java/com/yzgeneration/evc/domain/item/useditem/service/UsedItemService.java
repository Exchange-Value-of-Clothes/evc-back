package com.yzgeneration.evc.domain.item.useditem.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.implement.ItemImageAppender;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemListResponse.GetUsedItemListResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.implement.UsedItemReader;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsedItemService {
    private final UsedItemRepository usedItemRepository;
    private final ItemImageAppender itemImageAppender;
    private final UsedItemReader usedItemReader;

    private final ItemType itemType = ItemType.USEDITEM;

    public void createUsedItem(Long memberId, CreateUsedItemRequest createUsedItemRequest) {
        UsedItem usedItem = usedItemRepository.save(UsedItem.create(memberId, createUsedItemRequest, LocalDateTime.now()));
        itemImageAppender.createItemImages(usedItem.getId(), itemType, createUsedItemRequest.getImageNames());
    }

    public SliceResponse<GetUsedItemListResponse> getUsedItems(LocalDateTime cursor) {
        return usedItemRepository.getUsedItemList(cursor);
    }


    public GetUsedItemResponse getUsedItem(Long memberId, Long usedItemId) {
        return usedItemReader.getUsedItemResponse(memberId, usedItemId);
    }

    public SliceResponse<GetUsedItemListResponse> searchUsedItems(String q, LocalDateTime cursor){
        return usedItemRepository.searchUsedItemList(q, cursor);
    }
}
