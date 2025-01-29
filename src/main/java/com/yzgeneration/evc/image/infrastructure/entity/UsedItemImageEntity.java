package com.yzgeneration.evc.image.infrastructure.entity;

import com.yzgeneration.evc.image.model.UsedItemImage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Table(name = "used_item_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsedItemImageEntity extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long usedItemId;

    public static UsedItemImageEntity from(UsedItemImage usedItemImage) {
        return UsedItemImageEntity.builder()
                .usedItemId(usedItemImage.getUsedItemId())
                .imageName(usedItemImage.getImageName())
                .imageURL(usedItemImage.getImageURL())
                .build();
    }

    public UsedItemImage toModel() {
        return UsedItemImage.builder()
                .id(id)
                .usedItemId(usedItemId)
                .imageName(getImageName())
                .imageURL(getImageURL())
                .build();
    }
}
