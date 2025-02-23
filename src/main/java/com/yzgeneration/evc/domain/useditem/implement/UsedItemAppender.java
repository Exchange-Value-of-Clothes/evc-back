package com.yzgeneration.evc.domain.useditem.implement;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UsedItemAppender {
    private final UsedItemRepository usedItemRepository;

    public UsedItem createUsedItem(CreateUsedItemRequest createUsedItemRequest) {
        return usedItemRepository.save(UsedItem.create(createUsedItemRequest, LocalDateTime.now()));
    }
}
