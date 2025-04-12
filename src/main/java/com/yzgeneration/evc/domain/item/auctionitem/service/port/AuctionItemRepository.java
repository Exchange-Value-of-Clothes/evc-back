package com.yzgeneration.evc.domain.item.auctionitem.service.port;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.GetAuctionItemListResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;

import java.time.LocalDateTime;

public interface AuctionItemRepository {
    AuctionItem save(AuctionItem auctionItem);

    SliceResponse<GetAuctionItemListResponse> getAuctionItemList(Long memberId, LocalDateTime cursor);

    GetAuctionItemResponse findByIdAndMemberId(Long memberId, Long id);

    void updateCurrentPrice(Long id, int point);

    boolean checkMemberPointById(Long id, Long memberId, int point);

    boolean canMemberBidByIdAndMemberId(Long id, Long memberId);

    int getCurrentPriceById(Long auctionId);

    AuctionItem getById(Long id);
}
