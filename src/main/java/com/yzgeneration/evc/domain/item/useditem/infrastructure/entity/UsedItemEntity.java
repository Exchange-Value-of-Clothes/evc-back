package com.yzgeneration.evc.domain.item.useditem.infrastructure.entity;

import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "used_items")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsedItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Embedded
    private ItemDetailsEntity itemDetailsEntity;

    @Embedded
    private UsedItemTransactionEntity usedItemTransactionEntity;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @Embedded
    private ItemStatsEntity itemStatsEntity;

    private LocalDateTime createdAt;

    public static UsedItemEntity from(UsedItem usedItem) {
        return UsedItemEntity.builder()
                .memberId(usedItem.getMemberId())
                .itemDetailsEntity(ItemDetailsEntity.from(usedItem.getItemDetails()))
                .usedItemTransactionEntity(UsedItemTransactionEntity.from(usedItem.getUsedItemTransaction()))
                .itemStatus(usedItem.getItemStatus())
                .itemStatsEntity(ItemStatsEntity.from(usedItem.getItemStats()))
                .createdAt(usedItem.getCreatedAt()).build();
    }

    public UsedItem toModel() {
        return UsedItem.builder()
                .id(id)
                .memberId(memberId)
                .itemDetails(itemDetailsEntity.toModel())
                .usedItemTransaction(usedItemTransactionEntity.toModel())
                .itemStatus(itemStatus)
                .itemStats(itemStatsEntity.toModel())
                .createdAt(createdAt)
                .build();
    }
}
