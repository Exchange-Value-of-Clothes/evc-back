package com.yzgeneration.evc.domain.useditem.infrastructure.entity;

import com.yzgeneration.evc.domain.useditem.model.ItemStats;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemStatsEntity {
    private int viewCount;
    private int likeCount;
    private int chattingCount;

    public static ItemStatsEntity from(ItemStats itemStats) {
        return ItemStatsEntity.builder()
                .viewCount(itemStats.getViewCount())
                .likeCount(itemStats.getLikeCount())
                .chattingCount(itemStats.getChattingCount())
                .build();
    }

    public ItemStats toModel() {
        return ItemStats.builder()
                .viewCount(viewCount)
                .likeCount(likeCount)
                .chattingCount(chattingCount)
                .build();
    }
}
