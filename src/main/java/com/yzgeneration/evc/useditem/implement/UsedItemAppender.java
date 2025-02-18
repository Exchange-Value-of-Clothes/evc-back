package com.yzgeneration.evc.useditem.implement;

import com.yzgeneration.evc.common.service.port.TimeProvider;
import com.yzgeneration.evc.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.useditem.model.UsedItem;
import com.yzgeneration.evc.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsedItemAppender {
    private final UsedItemRepository usedItemRepository;
    private final TimeProvider timeProvider;

    public UsedItem createUsedItem(CreateUsedItem createUsedItem) {
        return usedItemRepository.save(UsedItem.create(createUsedItem, timeProvider.now()));
    }
}
