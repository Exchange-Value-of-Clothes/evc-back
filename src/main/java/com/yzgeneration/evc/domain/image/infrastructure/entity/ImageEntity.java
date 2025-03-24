package com.yzgeneration.evc.domain.image.infrastructure.entity;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.model.Image;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    private ItemType itemType;

    private String imageName;

    private boolean isThumbnail;

    public static ImageEntity from(Image image) {
        return ImageEntity.builder()
                .itemId(image.getItemId())
                .itemType(image.getItemType())
                .imageName(image.getImageName())
                .isThumbnail(image.isThumbnail())
                .build();
    }

    public Image toModel() {
        return Image.builder()
                .id(id)
                .itemId(itemId)
                .itemType(itemType)
                .imageName(imageName)
                .isThumbnail(isThumbnail)
                .build();
    }
}
