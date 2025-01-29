package com.yzgeneration.evc.useditem.dto;

import com.yzgeneration.evc.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.useditem.model.ItemDetails;
import com.yzgeneration.evc.useditem.model.ItemStats;
import com.yzgeneration.evc.useditem.model.UsedItem;
import com.yzgeneration.evc.useditem.model.UsedItemTransaction;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UsedItemResponse(Long memberId, ItemDetails itemDetails, UsedItemTransaction usedItemTransaction,
                               UsedItemStatus usedItemStatus, ItemStats itemStats, List<String> imageURLs,
                               LocalDateTime createAt) {
    public static UsedItemResponse of(UsedItem usedItem, List<String> imageURLs){
        return UsedItemResponse.builder()
                .memberId(usedItem.getId())
                .itemDetails(usedItem.getItemDetails())
                .usedItemTransaction(usedItem.getUsedItemTransaction())
                .usedItemStatus(usedItem.getUsedItemStatus())
                .itemStats(usedItem.getItemStats())
                .imageURLs(imageURLs)
                .createAt(usedItem.getCreatedAt())
                .build();
    }
}
