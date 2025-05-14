package com.yzgeneration.evc.domain.item.auctionitem.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.implement.ItemImageAppender;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.GetAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.GetMyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.MyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.implement.AuctionItemReader;
import com.yzgeneration.evc.domain.item.auctionitem.implement.AuctionItemStatusUpdater;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.implement.ItemCounter;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionItemService {
    private final AuctionItemRepository auctionItemRepository;
    private final AuctionItemReader auctionItemReader;
    private final ItemImageAppender itemImageAppender;
    private final ItemCounter itemCounter;
    private final AuctionItemStatusUpdater auctionItemStatusUpdater;
    private final ItemType itemType = ItemType.AUCTIONITEM;

    public void createAuctionItem(Long memberId, CreateAuctionItemRequest createAuctionItemRequest) {
        AuctionItem auctionItem = auctionItemRepository.save(AuctionItem.create(memberId, createAuctionItemRequest));
        itemImageAppender.createItemImages(auctionItem.getId(), itemType, createAuctionItemRequest.getImageNames());
    }

    public SliceResponse<GetAuctionItemsResponse> getAuctionItems(Long memberId, LocalDateTime cursor) {
        return auctionItemRepository.getAuctionItems(memberId, cursor);
    }

    public GetAuctionItemResponse getAuctionItem(Long memberId, Long itemId) {
        return auctionItemReader.getAuctionItemResponse(memberId, itemId);
    }

    public SliceResponse<GetAuctionItemsResponse> searchAuctionItems(String q, Long memberId, LocalDateTime cursor) {
        return auctionItemRepository.searchAuctionItems(q, memberId, cursor);
    }

    public MyOrMemberAuctionItemsResponse getMyOrMemberAuctionItems(Long memberId, LocalDateTime cursor) {
        Long postItemCount = itemCounter.countPostItem(memberId);
        SliceResponse<GetMyOrMemberAuctionItemsResponse> myOrMemberAuctionItems = auctionItemRepository.getMyOrMemberAuctionItems(memberId, cursor);
        return new MyOrMemberAuctionItemsResponse(postItemCount, myOrMemberAuctionItems);
    }

    public void deleteAuctionItem(Long memberId, Long auctionItemId) {
        auctionItemStatusUpdater.updateItemStatus(memberId, auctionItemId, ItemStatus.DELETED);
    }
}
