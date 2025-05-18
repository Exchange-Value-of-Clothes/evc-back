package com.yzgeneration.evc.domain.like.model;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Like {
    private Long id;

    private Long memberId;

    private Long itemId;

    private ItemType itemType;

    private LocalDateTime createAt;

    public static Like create(Long memberId, Long itmeId, ItemType itemType) {
        return Like.builder()
                .memberId(memberId)
                .itemId(itmeId)
                .itemType(itemType)
                .createAt(LocalDateTime.now())
                .build();
    }
}
