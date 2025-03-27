package com.yzgeneration.evc.mock.image;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.model.ItemImage;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakeItemImageRepository implements ItemImageRepository {

    private static Long autoGeneratedId = 0L;
    private List<ItemImage> mockItemImages = new ArrayList<>();

    @Override
    public void saveAll(List<ItemImage> itemImages) {
        mockItemImages = itemImages.stream().map(
                image -> ItemImage.builder()
                        .id(++autoGeneratedId)
                        .itemId(image.getItemId())
                        .itemType(image.getItemType())
                        .imageName(image.getImageName())
                        .isThumbnail(image.isThumbnail())
                        .build()
        ).toList();
    }


    @Override
    public List<String> findImageNamesByItemIdAndItemType(Long itemId, ItemType itemType) {
        return mockItemImages.stream()
                .filter(image -> Objects.equals(image.getId(), itemId) && image.getItemType().equals(itemType))
                .map(ItemImage::getImageName).toList();
    }
}