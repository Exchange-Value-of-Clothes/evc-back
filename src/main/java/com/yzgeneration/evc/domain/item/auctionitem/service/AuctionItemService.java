package com.yzgeneration.evc.domain.item.auctionitem.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.implement.ItemImageAppender;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.GetAuctionItemListResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.implement.AuctionItemReader;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionItemService {
    private final AuctionItemRepository auctionItemRepository;
    private final AuctionItemReader auctionItemReader;
    private final ItemImageAppender itemImageAppender;
    private final ItemType itemType = ItemType.AUCTIONITEM;

    public void createAuctionItem(Long memberId, CreateAuctionItemRequest createAuctionItemRequest) {
        AuctionItem auctionItem = auctionItemRepository.save(AuctionItem.create(memberId, createAuctionItemRequest));
        itemImageAppender.createItemImages(auctionItem.getId(), itemType, createAuctionItemRequest.getImageNames());

    }

    public SliceResponse<GetAuctionItemListResponse> getAuctionItems(Long memberId, LocalDateTime cursor) {
        return auctionItemRepository.getAuctionItemList(memberId, cursor);
    }

    public GetAuctionItemResponse getAuctionItem(Long memberId, Long itemId) {
        return auctionItemReader.getAuctionItemResponse(memberId, itemId);
    }
}
