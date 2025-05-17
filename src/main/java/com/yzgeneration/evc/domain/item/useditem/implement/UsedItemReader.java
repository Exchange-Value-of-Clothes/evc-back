package com.yzgeneration.evc.domain.item.useditem.implement;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetMyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.domain.like.service.port.LikeRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UsedItemReader {
    private final UsedItemRepository usedItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final LikeRepository likeRepository;
    private final ItemType ITEM_TYPE = ItemType.USEDITEM;

    public SliceResponse<GetUsedItemsResponse> getUsedItems(LocalDateTime cursor, Long memberId) {
        SliceResponse<GetUsedItemsResponse> response = usedItemRepository.getUsedItems(cursor, memberId);
        response.getContent().forEach(res -> {
            Long count = likeRepository.countByItemIdAndItemType(res.getUsedItemId(), ITEM_TYPE);
            res.setLikeCount(count);
        });
        return response;
    }

    public GetUsedItemResponse getUsedItem(Long memberId, Long usedItemId) {
        GetUsedItemResponse getUsedItemResponse = usedItemRepository.findUsedItemByMemberIdAndUsedItemId(memberId, usedItemId);

        List<String> imageNames = itemImageRepository.findImageNamesByItemIdAndItemType(usedItemId, ITEM_TYPE);
        getUsedItemResponse.setImageNames(imageNames);

        Long count = likeRepository.countByItemIdAndItemType(usedItemId, ITEM_TYPE);
        getUsedItemResponse.setViewCount(count);

        return getUsedItemResponse;
    }

    public SliceResponse<GetUsedItemsResponse> getUsedItemsBySearch(String q, LocalDateTime cursor) {
        SliceResponse<GetUsedItemsResponse> response = usedItemRepository.searchUsedItems(q, cursor);
        response.getContent().forEach(res -> {
            Long count = likeRepository.countByItemIdAndItemType(res.getUsedItemId(), ITEM_TYPE);
            res.setLikeCount(count);
        });
        return response;
    }

    public SliceResponse<GetMyOrMemberUsedItemsResponse> getMyOrMemberUsedItems(Long memberId, LocalDateTime cursor, TransactionMode transactionMode) {
        SliceResponse<GetMyOrMemberUsedItemsResponse> response = usedItemRepository.getMyOrMemberUsedItems(memberId, cursor, transactionMode);
        response.getContent().forEach(res -> {
            Long count = likeRepository.countByItemIdAndItemType(res.getUsedItemId(), ITEM_TYPE);
            res.setLikeCount(count);
        });
        return response;
    }
}
