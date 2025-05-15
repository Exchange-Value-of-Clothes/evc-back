package com.yzgeneration.evc.domain.item.auctionitem.implement;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.GetMyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.like.service.port.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionItemReader {
    private final AuctionItemRepository auctionItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final LikeRepository likeRepository;
    private final ItemType ITEM_TYPE = ItemType.AUCTIONITEM;

    public GetAuctionItemResponse getAuctionItemResponse(Long memberId, Long itemId) {
        List<String> imageNameList = itemImageRepository.findImageNamesByItemIdAndItemType(itemId, ITEM_TYPE);

        GetAuctionItemResponse auctionItemResponse = auctionItemRepository.findAuctionItemByMemberIdAndId(memberId, itemId);
        auctionItemResponse.setImageNameList(imageNameList);

        Long count = likeRepository.countByItemIdAndItemType(itemId, ITEM_TYPE);
        auctionItemResponse.getAuctionItemStatsResponse().setLikeCount(count);

        return auctionItemResponse;
    }

    public SliceResponse<GetMyOrMemberAuctionItemsResponse> getMyOrMemberAuctionItems(Long memberId, LocalDateTime cursor) {
        SliceResponse<GetMyOrMemberAuctionItemsResponse> response = auctionItemRepository.getMyOrMemberAuctionItems(memberId, cursor);
        response.getContent().forEach(res -> {
            Long count = likeRepository.countByItemIdAndItemType(res.getAuctionItemId(), ITEM_TYPE);
            res.setLikeCount(count);
        });
        return response;
    }
}
