package com.yzgeneration.evc.mock.usedItem;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemListResponse.GetUsedItemListResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FakeUsedItemRepository implements UsedItemRepository {

    private static Long autoGeneratedId = 0L;
    private static final List<UsedItem> mockUsedItems = new ArrayList<>();

    @Override
    public UsedItem save(UsedItem usedItem) {
        if (usedItem.getId() == null) {
            usedItem = UsedItem.builder()
                    .id(++autoGeneratedId)
                    .memberId(usedItem.getMemberId())
                    .itemDetails(usedItem.getItemDetails())
                    .usedItemTransaction(usedItem.getUsedItemTransaction())
                    .itemStatus(usedItem.getItemStatus())
                    .itemStats(usedItem.getItemStats())
                    .createdAt(usedItem.getCreatedAt())
                    .build();
        }

        mockUsedItems.add(usedItem);
        return usedItem;
    }

    @Override
    public SliceResponse<GetUsedItemListResponse> getUsedItemList(LocalDateTime cursor) {
        int size = 10;

        List<UsedItem> usedItemList = new ArrayList<>(mockUsedItems.stream()
                .filter(usedItem -> cursor == null || usedItem.getCreatedAt().isBefore(cursor))
                .sorted(Comparator.comparing(UsedItem::getCreatedAt).reversed())
                .limit(size + 1)
                .toList());

        boolean hasNext = usedItemList.size() > size;
        if (hasNext) {
            usedItemList.remove(size);
        }

        List<GetUsedItemListResponse> usedItemListResponses = usedItemList.stream().map(
                usedItem -> new GetUsedItemListResponse(usedItem.getId(), usedItem.getItemDetails().getTitle(), usedItem.getItemDetails().getPrice(), usedItem.getUsedItemTransaction().getTransactionMode(), usedItem.getUsedItemTransaction().getTransactionStatus(), "imageName.jpg",
                        usedItem.getItemStats().getLikeCount(), usedItem.getCreatedAt(), usedItem.getItemStatus())
        ).toList();

        LocalDateTime localCreateAt = !usedItemListResponses.isEmpty() ? usedItemListResponses.get(usedItemListResponses.size() - 1).getCreateAt() : null;

        return new SliceResponse<>(new SliceImpl<>(usedItemListResponses, PageRequest.of(0, size), hasNext), localCreateAt);
    }

    @Override
    public Optional<GetUsedItemResponse> findByMemberIdAndUsedItemId(Long memberId, Long usedItemId) {
        return mockUsedItems.stream()
                .filter(usedItem -> usedItem.getId().equals(usedItemId))
                .findFirst()
                .map(usedItem -> {
                    List<String> imageNameList = List.of("imageName.jpg");
                    return new GetUsedItemResponse(usedItem.getItemDetails().getTitle(), usedItem.getItemDetails().getCategory(), usedItem.getItemDetails().getContent(), usedItem.getItemDetails().getPrice(), usedItem.getUsedItemTransaction().getTransactionType(), usedItem.getUsedItemTransaction().getTransactionMode(), usedItem.getUsedItemTransaction().getTransactionStatus(), imageNameList,
                            usedItem.getItemStats().getViewCount(), usedItem.getItemStats().getLikeCount(), usedItem.getItemStats().getChattingCount(), usedItem.getMemberId(), "marketNickname", usedItem.getMemberId().equals(memberId), usedItem.getCreatedAt(), usedItem.getItemStatus());
                });
    }
}
