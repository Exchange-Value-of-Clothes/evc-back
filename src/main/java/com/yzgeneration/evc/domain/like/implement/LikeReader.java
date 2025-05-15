package com.yzgeneration.evc.domain.like.implement;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.domain.like.dto.LikeItemsResponse;
import com.yzgeneration.evc.domain.like.model.Like;
import com.yzgeneration.evc.domain.like.service.port.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeReader {
    private final LikeRepository likeRepository;
    private final UsedItemRepository usedItemRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final static int SIZE = 10;

    public SliceResponse<LikeItemsResponse> getMyLikedItems(Long memberId) {
        SliceResponse<Like> likes = likeRepository.getLikesByMemberIdAndSize(memberId, SIZE);

        List<LikeItemsResponse> likeItems = likes.getContent().stream().map(like -> {
            Long count = likeRepository.countByItemIdAndItemType(like.getItemId(), like.getItemType());
            String imageName = itemImageRepository.findThumbNailImageNameByItemIdAndItemType(like.getItemId(), like.getItemType());

            if (like.getItemType().equals(ItemType.USEDITEM)) {
                UsedItem usedItem = usedItemRepository.getById(like.getItemId());
                return LikeItemsResponse.builder()
                        .itemId(usedItem.getId())
                        .title(usedItem.getItemDetails().getTitle())
                        .price(usedItem.getItemDetails().getPrice())
                        .transactionMode(usedItem.getUsedItemTransaction().getTransactionMode())
                        .transactionStatus(usedItem.getUsedItemTransaction().getTransactionStatus())
                        .imageName(imageName)
                        .likeCount(count)
                        .createAt(usedItem.getCreatedAt())
                        .build();
            } else {
                AuctionItem auctionItem = auctionItemRepository.getById(like.getItemId());
                return LikeItemsResponse.builder()
                        .itemId(auctionItem.getId())
                        .title(auctionItem.getAuctionItemDetails().getTitle())
                        .price(auctionItem.getAuctionItemPriceDetails().getCurrentPrice())
                        .transactionMode(auctionItem.getTransactionMode())
                        .transactionStatus(auctionItem.getTransactionStatus())
                        .imageName(imageName)
                        .likeCount(count)
                        .createAt(auctionItem.getStartTime())
                        .build();
            }
        }).toList();

        return new SliceResponse<>(
                new SliceImpl<>(likeItems, PageRequest.of(0, SIZE), likes.isHasNext()), likes.getCursor());
    }
}
