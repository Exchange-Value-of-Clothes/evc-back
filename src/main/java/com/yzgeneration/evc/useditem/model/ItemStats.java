package com.yzgeneration.evc.useditem.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemStats {
    @Builder.Default
    private int viewCount = 0;
    @Builder.Default
    private int likeCount = 0;
    @Builder.Default
    private int chattingCount = 0;

    public static ItemStats create() {
        return ItemStats.builder().build();
    }
}
