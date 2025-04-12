package com.yzgeneration.evc.domain.item.auctionitem.implement;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionItemReader {
    private final AuctionItemRepository auctionItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemType itemType = ItemType.AUCTIONITEM;

    public GetAuctionItemResponse getAuctionItemResponse(Long memberId, Long itemId) {

        List<String> imageNameList = itemImageRepository.findImageNamesByItemIdAndItemType(itemId, itemType);
        GetAuctionItemResponse auctionItemResponse = auctionItemRepository.findByIdAndMemberId(memberId, itemId);
        auctionItemResponse.setImageNameList(imageNameList);
        return auctionItemResponse;
    }
}
