package com.yzgeneration.evc.domain.useditem.implement;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UsedItemAppender {
    private final UsedItemRepository usedItemRepository;

    //    public UsedItem createUsedItem(Long memberId, CreateUsedItem createUsedItem) {
//        return usedItemRepository.save(UsedItem.create(memberId, createUsedItem, timeProvider.now()));
//    }
    public UsedItem createUsedItem( CreateUsedItem createUsedItem) {
        return usedItemRepository.save(UsedItem.create(createUsedItem, LocalDateTime.now()));
    }
}
