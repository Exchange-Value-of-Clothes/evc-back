package com.yzgeneration.evc.domain.item.useditem.service.port;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetMyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UsedItemRepository {
    UsedItem save(UsedItem usedItem);

    SliceResponse<GetUsedItemsResponse> getUsedItems(LocalDateTime cursor);

    Optional<GetUsedItemResponse> findUsedItemByMemberIdAndUsedItemId(Long memberId, Long usedItemId);

    UsedItem getById(Long id);

    SliceResponse<GetUsedItemsResponse> searchUsedItems(String q, LocalDateTime cursor);

    SliceResponse<GetMyOrMemberUsedItemsResponse> getMyOrMemberUsedItems(Long memberId, LocalDateTime cursor, TransactionMode transactionMode);

    Long countUsedItemByMemberId(Long memberId);
}