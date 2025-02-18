package com.yzgeneration.evc.useditem.implement;

import com.yzgeneration.evc.image.service.port.UsedItemImageRepository;
import com.yzgeneration.evc.useditem.service.port.UsedItemRepository;
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
