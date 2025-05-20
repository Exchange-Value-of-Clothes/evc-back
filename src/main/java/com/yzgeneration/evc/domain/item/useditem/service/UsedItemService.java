package com.yzgeneration.evc.domain.item.useditem.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.implement.ItemImageAppender;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.implement.ItemCounter;
import com.yzgeneration.evc.domain.item.useditem.dto.MyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetMyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.implement.UsedItemReader;
import com.yzgeneration.evc.domain.item.useditem.implement.UsedItemStatusUpdater;
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
    private final ItemCounter itemCounter;
    private final UsedItemStatusUpdater usedItemStatusUpdater;

    private final ItemType itemType = ItemType.USEDITEM;

    public void createUsedItem(Long memberId, CreateUsedItemRequest createUsedItemRequest) {
        UsedItem usedItem = usedItemRepository.save(UsedItem.create(memberId, createUsedItemRequest, LocalDateTime.now()));
        itemImageAppender.createItemImages(usedItem.getId(), itemType, createUsedItemRequest.getImageNames(), createUsedItemRequest.getImageNames().get(0));
    }

    public SliceResponse<GetUsedItemsResponse> getUsedItems(LocalDateTime cursor, Long memberId) {
        return usedItemReader.getUsedItems(cursor, memberId);
    }


    public GetUsedItemResponse getUsedItem(Long memberId, Long usedItemId) {
        return usedItemReader.getUsedItem(memberId, usedItemId);
    }

    public SliceResponse<GetUsedItemsResponse> searchUsedItems(String q, LocalDateTime cursor, Long memberId) {
        return usedItemReader.getUsedItemsBySearch(q, cursor, memberId);
    }

    public MyOrMemberUsedItemsResponse getMyOrMemberUsedItems(Long memberId, LocalDateTime cursor, TransactionMode transactionMode) {
        Long postItemCount = itemCounter.countPostItem(memberId);
        SliceResponse<GetMyOrMemberUsedItemsResponse> myOrMemberUsedItems = usedItemReader.getMyOrMemberUsedItems(memberId, cursor, transactionMode);
        return new MyOrMemberUsedItemsResponse(postItemCount, myOrMemberUsedItems);
    }

    public void updateTransactionStatus(Long memberId, Long usedItemId, TransactionStatus transactionStatus) {
        usedItemStatusUpdater.updateTransactionStatus(memberId, usedItemId, transactionStatus);
    }

    public void deleteUsedItem(Long memberId, Long usedItemId) {
        usedItemStatusUpdater.updateItemStatus(memberId, usedItemId, ItemStatus.DELETED);
    }
}
