package com.yzgeneration.evc.domain.useditem.implement;

import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.awt.print.Pageable;

@Component
@RequiredArgsConstructor
public class UsedItemLoader {
    private final UsedItemRepository usedItemRepository;
    private final UsedItemImageRepository usedItemImageRepository;

    private final int SIZE = 10;

    public void loadAllUsedItems(int page){
//        Pageable pageable =
//        Page<UsedItem> usedItemPage = usedItemRepository.findAllByOrderByCreatedAtDesc()
    }

    public void loadUsedItem(){

    }
}
