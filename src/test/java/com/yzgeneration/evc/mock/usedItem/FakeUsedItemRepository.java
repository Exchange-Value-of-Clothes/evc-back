package com.yzgeneration.evc.mock.usedItem;

import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FakeUsedItemRepository implements UsedItemRepository {

    private static Long autoGeneratedId = 0L;
    private static final List<UsedItem> mockUsedItems = new ArrayList<>();

    @Override
    public UsedItem save(UsedItem usedItem) {
        UsedItem mockUsedItem = UsedItem.builder()
                .id(++autoGeneratedId)
                .memberId(usedItem.getMemberId())
                .itemDetails(usedItem.getItemDetails())
                .usedItemTransaction(usedItem.getUsedItemTransaction())
                .itemStatus(usedItem.getItemStatus())
                .usedItemTransaction(usedItem.getUsedItemTransaction())
                .itemStats(usedItem.getItemStats())
                .createdAt(usedItem.getCreatedAt())
                .build();
        mockUsedItems.add(mockUsedItem);
        return mockUsedItem;
    }

    @Override
    public Slice<UsedItem> findAll(Pageable pageable) {
        List<UsedItem> sortedUsedItemList = mockUsedItems.stream()
                .sorted(Comparator.comparing(UsedItem::getCreatedAt).reversed())
                .toList();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sortedUsedItemList.size());
        List<UsedItem> pagedList = sortedUsedItemList.subList(start, end);
        boolean hasNext = end < sortedUsedItemList.size();

        return new SliceImpl<>(pagedList, pageable, hasNext);
    }

    @Override
    public String findNicknameByUsedItemId(Long usedItemId) {
        return "highyun";
    }


    @Override
    public UsedItem findById(Long usedItemId) {
        return mockUsedItems.stream()
                .filter(mockUsedItem -> Objects.equals(mockUsedItem.getId(), usedItemId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.USEDITEM_NOT_FOUND));
    }
}
