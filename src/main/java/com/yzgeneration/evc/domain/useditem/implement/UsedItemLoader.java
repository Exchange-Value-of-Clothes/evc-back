package com.yzgeneration.evc.domain.useditem.implement;

import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsedItemLoader {
    private final UsedItemRepository usedItemRepository;
    static int SIZE = 10;

    public Slice<UsedItem> loadUsedItemSlice(int page) {
        PageRequest pageRequest = PageRequest.of(page, SIZE);
        return usedItemRepository.findAll(pageRequest);

    }

    public UsedItem loadUsedItem(Long usedItemId) {
        return usedItemRepository.findById(usedItemId);
    }

    public String loadNicknameByUsedItemId(Long usedItemId) {
        return usedItemRepository.findNicknameByUsedItemId(usedItemId);
    }
}
