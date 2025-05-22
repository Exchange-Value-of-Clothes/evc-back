package com.yzgeneration.evc.domain.image.infrastructure.entity;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.image.model.ItemImage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "item_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String imageName;

    private boolean isThumbnail;

    public static ItemImageEntity from(ItemImage itemImage) {
        return ItemImageEntity.builder()
                .id(itemImage.getId())
                .itemId(itemImage.getItemId())
                .itemType(itemImage.getItemType())
                .imageName(itemImage.getImageName())
                .isThumbnail(itemImage.isThumbnail())
                .build();
    }

    public ItemImage toModel() {
        return ItemImage.builder()
                .id(id)
                .itemId(itemId)
                .itemType(itemType)
                .imageName(imageName)
                .isThumbnail(isThumbnail)
                .build();
    }
}
