package com.yzgeneration.evc.useditem.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.yzgeneration.evc.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.useditem.model.ItemDetails;
import com.yzgeneration.evc.useditem.model.ItemStats;
import com.yzgeneration.evc.useditem.model.UsedItem;
import com.yzgeneration.evc.useditem.model.UsedItemTransaction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UsedItemResponse {
    private Long memberId;

    @JsonUnwrapped
    private ItemDetails itemDetails;

    @JsonUnwrapped
    private UsedItemTransaction usedItemTransaction;

    private UsedItemStatus usedItemStatus;

    @JsonUnwrapped
    private ItemStats itemStats;

    private List<String> imageURLs;

    private LocalDateTime createAt;

    public static UsedItemResponse of(UsedItem usedItem, List<String> usedItemImageURLs) {
        return UsedItemResponse.builder()
                .memberId(usedItem.getId())
                .itemDetails(usedItem.getItemDetails())
                .usedItemTransaction(usedItem.getUsedItemTransaction())
                .usedItemStatus(usedItem.getUsedItemStatus())
                .itemStats(usedItem.getItemStats())
                .imageURLs(usedItemImageURLs)
                .createAt(usedItem.getCreatedAt())
                .build();
    }
}
