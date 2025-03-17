package com.yzgeneration.evc.domain.image.infrastructure.entity;

import com.yzgeneration.evc.domain.image.model.UsedItemImage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "used_item_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsedItemImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usedItemId;

    private String imageURL;

    private boolean isThumbnail;

    public static UsedItemImageEntity from(UsedItemImage usedItemImage) {
        return UsedItemImageEntity.builder()
                .usedItemId(usedItemImage.getUsedItemId())
                .imageURL(usedItemImage.getImageURL())
                .isThumbnail(usedItemImage.isThumbnail())
                .build();
    }

    public UsedItemImage toModel() {
        return UsedItemImage.builder()
                .id(id)
                .usedItemId(usedItemId)
                .imageURL(imageURL)
                .isThumbnail(isThumbnail)
                .build();
    }
}
