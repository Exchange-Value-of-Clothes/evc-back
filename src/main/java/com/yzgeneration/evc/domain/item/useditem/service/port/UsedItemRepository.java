package com.yzgeneration.evc.domain.item.useditem.service.port;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemListResponse.GetUsedItemListResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UsedItemRepository {
    UsedItem save(UsedItem usedItem);

    SliceResponse<GetUsedItemListResponse> getUsedItemList(LocalDateTime cursor);

    Optional<GetUsedItemResponse> findByMemberIdAndUsedItemId(Long memberId, Long usedItemId);

    UsedItem getById(Long id);

    SliceResponse<GetUsedItemListResponse> searchUsedItemList(String keyword, LocalDateTime cursor);
}