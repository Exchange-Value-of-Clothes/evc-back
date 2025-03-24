package com.yzgeneration.evc.domain.item.auctionitem.service.port;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.GetAuctionItemListResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuctionItemRepository {
    AuctionItem save(AuctionItem auctionItem);

    SliceResponse<GetAuctionItemListResponse> getAuctionItemList(Long memberId, LocalDateTime cursor);

    Optional<GetAuctionItemResponse> findByAuctionItemByItemId(Long memberId, Long itemId);
}
