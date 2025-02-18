package com.yzgeneration.evc.useditem.infrastructure.entity;

import com.yzgeneration.evc.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.useditem.model.UsedItem;
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

    @Embedded
    private ItemDetailsEntity itemDetailsEntity;

    @Embedded
    private UsedItemTransactionEntity usedItemTransactionEntity;

    @Enumerated(EnumType.STRING)
    private UsedItemStatus usedItemStatus;

    @Embedded
    private ItemStatsEntity itemStatsEntity;

    private LocalDateTime createdAt;

    public static UsedItemEntity from(UsedItem usedItem) {
        return UsedItemEntity.builder()
                .itemDetailsEntity(ItemDetailsEntity.from(usedItem.getItemDetails()))
                .usedItemTransactionEntity(UsedItemTransactionEntity.from(usedItem.getUsedItemTransaction()))
                .usedItemStatus(usedItem.getUsedItemStatus())
                .itemStatsEntity(ItemStatsEntity.from(usedItem.getItemStats()))
                .createdAt(usedItem.getCreatedAt()).build();
    }

    public UsedItem toModel() {
        return UsedItem.builder()
                .id(id)
                .itemDetails(itemDetailsEntity.toModel())
                .usedItemTransaction(usedItemTransactionEntity.toModel())
                .usedItemStatus(usedItemStatus)
                .itemStats(itemStatsEntity.toModel())
                .createdAt(createdAt)
                .build();
    }
}
