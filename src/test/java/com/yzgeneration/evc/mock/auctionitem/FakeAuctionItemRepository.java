package com.yzgeneration.evc.mock.auctionitem;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.AuctionItemPriceDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.GetAuctionItemListResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemStatsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FakeAuctionItemRepository implements AuctionItemRepository {

    private static Long autoGeneratedId = 0L;
    private static final List<AuctionItem> mockAuctionItem = new ArrayList<>();

    @Override
    public AuctionItem save(AuctionItem auctionItem) {
        if (auctionItem.getId() == null) {
            auctionItem = AuctionItem.builder()
                    .id(++autoGeneratedId)
                    .memberId(auctionItem.getMemberId())
                    .auctionItemDetails(auctionItem.getAuctionItemDetails())
                    .auctionItemStats(auctionItem.getAuctionItemStats())
                    .auctionItemPriceDetails(auctionItem.getAuctionItemPriceDetails())
                    .transactionStatus(auctionItem.getTransactionStatus())
                    .itemStatus(auctionItem.getItemStatus())
                    .startTime(auctionItem.getStartTime())
                    .endTime(auctionItem.getEndTime())
                    .build();
        }

        mockAuctionItem.add(auctionItem);
        return auctionItem;
    }

    @Override
    public SliceResponse<GetAuctionItemListResponse> getAuctionItemList(Long memberId, LocalDateTime cursor) {

        int size = 10;

        List<AuctionItem> auctionItemList = new ArrayList<>(mockAuctionItem.stream()
                .filter(auctionItem -> cursor == null || auctionItem.getStartTime().isBefore(cursor))
                .sorted(Comparator.comparing(AuctionItem::getStartTime).reversed())
                .limit(size + 1)
                .toList());

        boolean hasNext = auctionItemList.size() > size;
        if (hasNext) {
            auctionItemList.remove(size);
        }

        List<GetAuctionItemListResponse> auctionItemListResponses = auctionItemList.stream().map(
                auctionItem -> {
                    AuctionItemPriceDetailsResponse auctionItemPriceDetailsResponse = new AuctionItemPriceDetailsResponse(auctionItem.getAuctionItemPriceDetails().getStartPrice(), auctionItem.getAuctionItemPriceDetails().getCurrentPrice(), auctionItem.getAuctionItemPriceDetails().getBidPrice());
                    return new GetAuctionItemListResponse(auctionItem.getId(), auctionItem.getAuctionItemDetails().getTitle(), auctionItemPriceDetailsResponse, "imageName.jpg", auctionItem.getStartTime(), auctionItem.getEndTime(), 1000);
                }
        ).toList();

        LocalDateTime localStartTime = !auctionItemListResponses.isEmpty() ? auctionItemListResponses.get(auctionItemListResponses.size() - 1).getStartTime() : null;

        return new SliceResponse<>(new SliceImpl<>(auctionItemListResponses, PageRequest.of(0, size), hasNext), localStartTime);
    }

    @Override
    public Optional<GetAuctionItemResponse> findByAuctionItemByItemId(Long memberId, Long itemId) {
        return mockAuctionItem.stream()
                .filter(auction -> auction.getId().equals(itemId))
                .findFirst()
                .map(auctionItem -> {
                    AuctionItemDetailsResponse auctionItemDetailsResponse = new AuctionItemDetailsResponse(auctionItem.getAuctionItemDetails().getTitle(), auctionItem.getAuctionItemDetails().getCategory(), auctionItem.getAuctionItemDetails().getContent());
                    AuctionItemStatsResponse auctionItemStatsResponse = new AuctionItemStatsResponse(1, 1, 1);
                    List<String> imageNameList = List.of("imageName.jpg");

                    return new GetAuctionItemResponse(auctionItemDetailsResponse, auctionItemStatsResponse, imageNameList, auctionItem.getStartTime(), auctionItem.getEndTime(), auctionItem.getAuctionItemPriceDetails().getCurrentPrice(), auctionItem.getMemberId(), "marketNickname", auctionItem.getMemberId().equals(memberId), auctionItem.getItemStatus());
                });
    }
}
