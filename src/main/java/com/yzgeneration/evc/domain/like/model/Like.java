package com.yzgeneration.evc.domain.like.model;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Like {
    private Long id;

    private Long memberId;

    private Long itemId;

    private ItemType itemType;

    public static Like create(Long memberId, Long itmeId, ItemType itemType) {
        return Like.builder()
                .memberId(memberId)
                .itemId(itmeId)
                .itemType(itemType)
                .build();
    }
}
