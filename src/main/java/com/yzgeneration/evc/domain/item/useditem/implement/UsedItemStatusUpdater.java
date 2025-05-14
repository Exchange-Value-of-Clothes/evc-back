package com.yzgeneration.evc.domain.item.useditem.implement;

import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsedItemStatusUpdater {
    private final UsedItemRepository usedItemRepository;

    public void updateTransactionStatus(Long memberId, Long usedItemId, TransactionStatus transactionStatus) {
        UsedItem usedItem = usedItemRepository.getById(usedItemId);

        if (!usedItem.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.ITEM_ACCESS_DENIED);
        }
        usedItem.updateTransactionStatus(transactionStatus);
        usedItemRepository.save(usedItem);
    }

    public void updateItemStatus(Long memberId, Long usedItemId, ItemStatus itemStatus) {
        UsedItem usedItem = usedItemRepository.getById(usedItemId);

        if (!usedItem.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.ITEM_ACCESS_DENIED);
        }
        usedItem.updateItemStatus(itemStatus); //sofe delete 적용
        usedItemRepository.save(usedItem);
    }
}
