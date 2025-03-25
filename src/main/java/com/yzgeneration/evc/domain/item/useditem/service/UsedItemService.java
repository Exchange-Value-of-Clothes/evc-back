package com.yzgeneration.evc.domain.item.useditem.service;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.implement.ItemImageAppender;
import com.yzgeneration.evc.domain.image.service.port.ImageRepository;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemsDetails;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.implement.UsedItemAppender;
import com.yzgeneration.evc.domain.item.useditem.implement.UsedItemLoader;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsedItemService {
    //TODO implement 계층 -> repository 직접 사용으로 변경하기 (간단한 함수만 사용하기에)
    private final UsedItemAppender usedItemAppender;
    private final ItemImageAppender itemImageAppender;
    private final UsedItemLoader usedItemLoader;
    private final ImageRepository imageRepository;
    private final ItemType itemType = ItemType.USEDITEM;

    public void createUsedItem(Long memberId, CreateUsedItemRequest createUsedItemRequest) {
        UsedItem usedItem = usedItemAppender.createUsedItem(memberId, createUsedItemRequest);
        itemImageAppender.createItemImages(usedItem.getId(), ItemType.USEDITEM, createUsedItemRequest.getImageNames());
    }

    public GetUsedItemsResponse loadUsedItems(int page) {
        Slice<UsedItem> usedItemSlice = usedItemLoader.loadUsedItemSlice(page);
        List<UsedItem> usedItemList = usedItemSlice.getContent();

        List<GetUsedItemsDetails> loadUsedItemDetails = usedItemList.stream().map(
                usedItem -> GetUsedItemsDetails.create(usedItem, imageRepository.findThumbnailByItemIdAndItemType(usedItem.getId(), itemType))).toList();

        return GetUsedItemsResponse.builder()
                .loadUsedItemDetails(loadUsedItemDetails)
                .isLast(usedItemSlice.isLast())
                .build();
    }

    public GetUsedItemResponse loadUsedItem(Long memberId, Long usedItemId) {
        UsedItem usedItem = usedItemLoader.loadUsedItem(usedItemId);
        List<String> imageURLs = imageRepository.findImageNamesByItemIdAndItemType(usedItemId, itemType);
        String marketNickName = usedItemLoader.loadNicknameByUsedItemId(usedItemId);

        return GetUsedItemResponse.create(usedItem, imageURLs, marketNickName, memberId);
    }
}
