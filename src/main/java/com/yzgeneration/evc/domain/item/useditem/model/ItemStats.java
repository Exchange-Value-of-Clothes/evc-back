package com.yzgeneration.evc.domain.item.useditem.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemStats {

    private Long viewCount;

    private Long likeCount;

    private Long chattingCount;

    public static ItemStats create() {
        return ItemStats.builder().build();
    }
}
