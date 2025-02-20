package com.yzgeneration.evc.domain.useditem.implement;

import com.yzgeneration.evc.common.implement.port.TimeProvider;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsedItemAppender {
    private final UsedItemRepository usedItemRepository;
    private final TimeProvider timeProvider;

    public UsedItem createUsedItem(Long memberId, CreateUsedItem createUsedItem) {
        return usedItemRepository.save(UsedItem.create(memberId, createUsedItem, timeProvider.now()));
    }
}
