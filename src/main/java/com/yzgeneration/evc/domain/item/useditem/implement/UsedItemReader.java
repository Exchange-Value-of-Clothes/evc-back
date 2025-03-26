package com.yzgeneration.evc.domain.item.useditem.implement;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.service.port.ImageRepository;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsedItemReader {
    private final UsedItemRepository usedItemRepository;
    private final ImageRepository imageRepository;
    private final ItemType itemType = ItemType.USEDITEM;

    public GetUsedItemResponse getUsedItemResponse(Long memberId, Long usedItemId) {
        GetUsedItemResponse getUsedItemResponse = usedItemRepository.findByMemberIdAndUsedItemId(memberId, usedItemId).orElseThrow(
                () -> new CustomException(ErrorCode.USEDITEM_NOT_FOUND));

        List<String> imageNames = imageRepository.findImageNamesByItemIdAndItemType(usedItemId, itemType);
        getUsedItemResponse.setImageNames(imageNames);

        return getUsedItemResponse;
    }
}
