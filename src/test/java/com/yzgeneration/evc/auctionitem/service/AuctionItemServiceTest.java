package com.yzgeneration.evc.auctionitem.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.implement.ItemImageAppender;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.GetAuctionItemListResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.implement.AuctionItemReader;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.mock.auctionitem.FakeAuctionItemRepository;
import com.yzgeneration.evc.mock.image.FakeItemImageRepository;
import org.junit.jupiter.api.*;

import static com.yzgeneration.evc.fixture.AuctionItemFixture.fixCreateAuctionItemRequest;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuctionItemServiceTest {

    private AuctionItemService auctionItemService;

    @BeforeEach
    void init() {
        AuctionItemRepository auctionItemRepository = new FakeAuctionItemRepository();
        ItemImageRepository itemImageRepository = new FakeItemImageRepository();
        ItemImageAppender itemImageAppender = new ItemImageAppender(itemImageRepository);

        AuctionItemReader auctionItemReader = new AuctionItemReader(auctionItemRepository, itemImageRepository);

        auctionItemService = new AuctionItemService(auctionItemRepository, auctionItemReader, itemImageAppender);
    }

    @Test
    @Order(1)
    @DisplayName("경매상품 등록")
    void createAuctionItem() {
        //given
        Long memberId = 1L;
        CreateAuctionItemRequest createAuctionItemRequest = fixCreateAuctionItemRequest();

        //when
        //then
        auctionItemService.createAuctionItem(memberId, createAuctionItemRequest);
    }

    @Test
    @DisplayName("경매상품들 조회 (Slice)")
    void getAuctionItems() {
        //give
        Long memberId = 1L;

        //when
        SliceResponse<GetAuctionItemListResponse> auctionItemListResponse = auctionItemService.getAuctionItems(memberId, null);

        //then
        assertThat(auctionItemListResponse.getContent().get(0).getAuctionItemId()).isEqualTo(1L);
        assertThat(auctionItemListResponse.getContent().get(0).getTitle()).isEqualTo("title");
        assertThat(auctionItemListResponse.getContent().get(0).getAuctionItemPriceDetailsResponse().getStartPrice()).isEqualTo(5000);
        assertThat(auctionItemListResponse.getContent().get(0).getAuctionItemPriceDetailsResponse().getCurrentPrice()).isEqualTo(5000);
        assertThat(auctionItemListResponse.getContent().get(0).getAuctionItemPriceDetailsResponse().getBidPrice()).isEqualTo(1000);
        assertThat(auctionItemListResponse.getContent().get(0).getImageName()).isEqualTo("imageName.jpg");
    }

    @Test
    @DisplayName("경매상품 조회")
    void getAuctionItem() {
        //give
        Long memberId = 1L;
        Long itemId = 1L;

        //when
        GetAuctionItemResponse auctionItemResponse = auctionItemService.getAuctionItem(memberId, itemId);

        //then
        assertThat(auctionItemResponse.getAuctionItemDetailsResponse().getTitle()).isEqualTo("title");
        assertThat(auctionItemResponse.getAuctionItemStatsResponse().getLikeCount()).isEqualTo(1);
        assertThat(auctionItemResponse.getMarketMemberId()).isEqualTo(1L);
        assertThat(auctionItemResponse.getMarketNickname()).isEqualTo("marketNickname");
    }
}
