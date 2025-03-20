package com.yzgeneration.evc.mock.usedItem;

import com.yzgeneration.evc.domain.image.model.UsedItemImage;
import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakeUsedItemImageRepository implements UsedItemImageRepository {

    private static Long autoGeneratedId = 0L;
    private List<UsedItemImage> mockUsedItemImages = new ArrayList<>();

    @Override
    public void saveAll(List<UsedItemImage> usedItemImages) {
        mockUsedItemImages = usedItemImages.stream().map(
                usedItemImage -> UsedItemImage.builder()
                        .id(++autoGeneratedId)
                        .usedItemId(usedItemImage.getUsedItemId())
                        .imageURL(usedItemImage.getImageURL())
                        .isThumbnail(usedItemImage.isThumbnail())
                        .build()
        ).toList();
    }


    @Override
    public List<String> findImageURLsByUsedItemId(Long usedItemId) {
        return mockUsedItemImages.stream()
                .filter(mockUsedItemImage -> Objects.equals(mockUsedItemImage.getUsedItemId(), usedItemId))
                .map(UsedItemImage::getImageURL).toList();
    }

    @Override
    public String findThumbnailByUsedItemId(Long usedItemId) {
        return mockUsedItemImages.stream()
                .filter(mockUsedItemImage -> Objects.equals(mockUsedItemImage.getUsedItemId(), usedItemId) && mockUsedItemImage.isThumbnail())
                .map(UsedItemImage::getImageURL).findFirst().orElse(null);
    }
}