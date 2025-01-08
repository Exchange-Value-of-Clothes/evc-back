package com.yzgeneration.evc.useditem.service;

import com.yzgeneration.evc.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.useditem.dto.UsedItemResponse;
import com.yzgeneration.evc.useditem.model.UsedItem;
import com.yzgeneration.evc.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsedItemService {
    private final UsedItemRepository usedItemRepository;

    public UsedItemResponse createUsedITem(CreateUsedItem createUsedItem) {
        return UsedItemResponse.from(usedItemRepository.save(UsedItem.create(createUsedItem)));
    }
}
