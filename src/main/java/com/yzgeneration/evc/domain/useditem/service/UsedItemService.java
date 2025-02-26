package com.yzgeneration.evc.domain.useditem.service;

import com.yzgeneration.evc.domain.image.implement.UsedItemImageAppender;
import com.yzgeneration.evc.domain.image.implement.UsedItemImageLoader;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.CreateUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsDetails;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsResponse;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemAppender;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemLoader;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsedItemService {
    private final UsedItemAppender usedItemAppender;
    private final UsedItemImageAppender usedItemImageAppender;
    private final UsedItemLoader usedItemLoader;
    private final UsedItemImageLoader usedItemImageLoader;

    public CreateUsedItemResponse createUsedItem(CreateUsedItemRequest createUsedItemRequest, List<MultipartFile> imageFiles) {
        UsedItem usedItem = usedItemAppender.createUsedItem(createUsedItemRequest);
        usedItemImageAppender.createUsedItemImages(usedItem.getId(), imageFiles);

        return new CreateUsedItemResponse(createUsedItemRequest.getMemberId(), usedItem.getId());
    }

    public LoadUsedItemsResponse loadUsedItems(int page) {
        Slice<UsedItem> usedItemSlice = usedItemLoader.loadUsedItemSlice(page);
        List<UsedItem> usedItemList = usedItemSlice.getContent();

        List<LoadUsedItemsDetails> loadUsedItemDetails = usedItemList.stream().map(
                usedItem -> LoadUsedItemsDetails.builder()
                        .usedItemId(usedItem.getId())
                        .title(usedItem.getItemDetails().getTitle())
                        .price(usedItem.getItemDetails().getPrice())
                        .transactionMode(usedItem.getUsedItemTransaction().getTransactionMode())
                        .transactionStatue(usedItem.getUsedItemTransaction().getTransactionStatue())
                        .imageURLs(usedItemImageLoader.loadUsedItemImages(usedItem.getId()))
                        .likeCount(usedItem.getItemStats().getLikeCount())
                        .createAt(usedItem.getCreatedAt())
                        .usedItemStatus(usedItem.getUsedItemStatus())
                        .build()
        ).toList();

        return LoadUsedItemsResponse.builder()
                .loadUsedItemDetails(loadUsedItemDetails)
                .isLast(usedItemSlice.isLast())
                .build();
    }

    public LoadUsedItemResponse loadUsedItem(Long memberId, Long usedItemId) {
        UsedItem usedItem = usedItemLoader.loadUsedItem(usedItemId);
        List<String> imageURLs = usedItemImageLoader.loadUsedItemImages(usedItemId);
        String nickName = usedItemLoader.loadNicknameByUsedItemId(usedItemId);

        return LoadUsedItemResponse.builder()
                .title(usedItem.getItemDetails().getTitle())
                .category(usedItem.getItemDetails().getCategory())
                .content(usedItem.getItemDetails().getContent())
                .price(usedItem.getItemDetails().getPrice())
                .transactionType(usedItem.getUsedItemTransaction().getTransactionType())
                .transactionMode(usedItem.getUsedItemTransaction().getTransactionMode())
                .transactionStatue(usedItem.getUsedItemTransaction().getTransactionStatue())
                .imageURLs(imageURLs)
                .viewCount(usedItem.getItemStats().getViewCount())
                .likeCount(usedItem.getItemStats().getLikeCount())
                .chattingCount(usedItem.getItemStats().getChattingCount())
                .memberId(usedItem.getMemberId())
                .nickName(nickName)
                .isOwned(usedItem.getMemberId().equals(memberId))
                .createAt(usedItem.getCreatedAt())
                .usedItemStatus(usedItem.getUsedItemStatus())
                .build();
    }
}
