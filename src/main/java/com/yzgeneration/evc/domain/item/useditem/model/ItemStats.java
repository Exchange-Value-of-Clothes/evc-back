package com.yzgeneration.evc.domain.item.useditem.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemStats {

    private int viewCount;

    private int likeCount;

    private int chattingCount;

    public static ItemStats create() {
        return ItemStats.builder().build();
    }
}
