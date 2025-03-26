package com.yzgeneration.evc.domain.item.auctionitem.implement;

import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.service.port.ImageRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionItemReader {
    private final AuctionItemRepository auctionItemRepository;
    private final ImageRepository imageRepository;
    private final ItemType itemType = ItemType.AUCTIONITEM;

    public GetAuctionItemResponse getAuctionItemResponse(Long memberId, Long itemId) {

        List<String> imageNameList = imageRepository.findImageNamesByItemIdAndItemType(itemId, itemType);
        GetAuctionItemResponse auctionItemResponse = auctionItemRepository.findByMemberIdAndAuctionItemId(memberId, itemId).orElseThrow(
                () -> new CustomException(ErrorCode.AUCTIONITEM_NOT_FOUND)
        );
        auctionItemResponse.setImageNameList(imageNameList);
        return auctionItemResponse;
    }
}
