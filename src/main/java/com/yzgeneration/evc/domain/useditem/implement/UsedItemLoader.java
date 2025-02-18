package com.yzgeneration.evc.domain.useditem.implement;

import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsedItemLoader {
    private final UsedItemRepository usedItemRepository;
    private final UsedItemImageRepository usedItemImageRepository;

    public void loadAllUsedItems(){

    }

    public void loadUsedItem(){

    }
}
