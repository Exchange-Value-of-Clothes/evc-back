package com.yzgeneration.evc.domain.useditem.service;

import com.yzgeneration.evc.domain.image.implement.UsedItemImageAppender;
import com.yzgeneration.evc.domain.image.implement.UsedItemImageLoader;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsDetails;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsResponse;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemAppender;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemLoader;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsedItemService {
    private final UsedItemAppender usedItemAppender;
    private final UsedItemImageAppender usedItemImageAppender;
    private final UsedItemLoader usedItemLoader;
    private final UsedItemImageLoader usedItemImageLoader;

    public void createUsedItem(Long memberId, CreateUsedItemRequest createUsedItemRequest) {
        UsedItem usedItem = usedItemAppender.createUsedItem(memberId, createUsedItemRequest);
        usedItemImageAppender.createUsedItemImages(usedItem.getId(), createUsedItemRequest.getImageNames());
    }

    public LoadUsedItemsResponse loadUsedItems(int page) {
        Slice<UsedItem> usedItemSlice = usedItemLoader.loadUsedItemSlice(page);
        List<UsedItem> usedItemList = usedItemSlice.getContent();

        List<LoadUsedItemsDetails> loadUsedItemDetails = usedItemList.stream().map(
                usedItem -> LoadUsedItemsDetails.create(usedItem, usedItemImageLoader.loadUsedItemImages(usedItem.getId()))).toList();

        return LoadUsedItemsResponse.builder()
                .loadUsedItemDetails(loadUsedItemDetails)
                .isLast(usedItemSlice.isLast())
                .build();
    }

    public LoadUsedItemResponse loadUsedItem(Long memberId, Long usedItemId) {
        UsedItem usedItem = usedItemLoader.loadUsedItem(usedItemId);
        List<String> imageURLs = usedItemImageLoader.loadUsedItemImages(usedItemId);
        String marketNickName = usedItemLoader.loadNicknameByUsedItemId(usedItemId);

        return LoadUsedItemResponse.create(usedItem, imageURLs, marketNickName, memberId);
    }
}
