package com.yzgeneration.evc.domain.like.infrastructure.entity;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.like.model.Like;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "likes")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long itemId;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    public static LikeEntity from(Like like) {
        return LikeEntity.builder()
                .id(like.getId())
                .memberId(like.getMemberId())
                .itemId(like.getItemId())
                .itemType(like.getItemType())
                .build();
    }

    public Like toModel() {
        return Like.builder()
                .id(id)
                .memberId(memberId)
                .itemId(itemId)
                .itemType(itemType)
                .build();
    }
}
