package com.yzgeneration.evc.usedItem.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.implement.ItemImageAppender;
import com.yzgeneration.evc.domain.image.service.port.ItemImageRepository;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.implement.ItemCounter;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.implement.UsedItemReader;
import com.yzgeneration.evc.domain.item.useditem.implement.UsedItemStatusUpdater;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.domain.like.service.port.LikeRepository;
import com.yzgeneration.evc.mock.auctionitem.FakeAuctionItemRepository;
import com.yzgeneration.evc.mock.image.FakeItemImageRepository;
import com.yzgeneration.evc.mock.like.FakeLikeRepository;
import com.yzgeneration.evc.mock.usedItem.FakeUsedItemRepository;
import org.junit.jupiter.api.*;

import static com.yzgeneration.evc.fixture.UsedItemFixture.fixCreateUsedItemRequest;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsedItemServiceTest {
    private UsedItemService usedItemService;

    @BeforeEach
    void init() {
        UsedItemRepository usedItemRepository = new FakeUsedItemRepository();
        AuctionItemRepository auctionItemRepository = new FakeAuctionItemRepository();
        ItemImageRepository itemImageRepository = new FakeItemImageRepository();
        LikeRepository likeRepository = new FakeLikeRepository();

        UsedItemReader usedItemReader = new UsedItemReader(usedItemRepository, itemImageRepository, likeRepository);
        ItemImageAppender itemImageAppender = new ItemImageAppender(itemImageRepository);
        ItemCounter itemCounter = new ItemCounter(usedItemRepository, auctionItemRepository);
        UsedItemStatusUpdater usedItemStatusUpdater = new UsedItemStatusUpdater(usedItemRepository);

        usedItemService = new UsedItemService(usedItemRepository, itemImageAppender, usedItemReader, itemCounter, usedItemStatusUpdater);
    }

    @Test
    @Order(1)
    @DisplayName("중고상품 등록이 정상적으로 되는지 체크")
    void createUsedItem() {
        //given
        Long memberId = 1L;
        CreateUsedItemRequest createUsedItemRequest = fixCreateUsedItemRequest();
        //when
        //then
        usedItemService.createUsedItem(memberId, createUsedItemRequest);
    }

    @Test
    @DisplayName("등록한 중고상품 리스트가 정상적으로 조회되는지 체크")
    void getUsedItems() {
        //given
        //when
        SliceResponse<GetUsedItemsResponse> usedItemListResponse = usedItemService.getUsedItems(null, null);

        //then
        assertThat(usedItemListResponse.getContent().get(0).getUsedItemId()).isEqualTo(1L);
        assertThat(usedItemListResponse.getContent().get(0).getTitle()).isEqualTo("title");
        assertThat(usedItemListResponse.getContent().get(0).getPrice()).isEqualTo(5000);
        assertThat(usedItemListResponse.getContent().get(0).getImageName()).isEqualTo("imageName.jpg");

    }

    @Test
    @DisplayName("등록한 중고상품이 정상적으로 조회되는지 체크")
    void getUsedItem() {
        //given
        Long memberId = 1L;
        Long usedItemId = 1L;

        //when
        GetUsedItemResponse getUsedItemResponse = usedItemService.getUsedItem(memberId, usedItemId);

        //then
        assertThat(getUsedItemResponse.getTitle()).isEqualTo("title");
        assertThat(getUsedItemResponse.getCategory()).isEqualTo("category");
        assertThat(getUsedItemResponse.getContent()).isEqualTo("content");
        assertThat(getUsedItemResponse.getPrice()).isEqualTo(5000);

        assertThat(getUsedItemResponse.getTransactionType()).isEqualTo(TransactionType.DIRECT);
        assertThat(getUsedItemResponse.getTransactionMode()).isEqualTo(TransactionMode.BUY);
        assertThat(getUsedItemResponse.getTransactionStatus()).isEqualTo(TransactionStatus.ONGOING);

        assertThat(getUsedItemResponse.getMarketMemberId()).isEqualTo(1L);
        assertThat(getUsedItemResponse.getIsOwned()).isEqualTo(true);
        assertThat(getUsedItemResponse.getItemStatus()).isEqualTo(ItemStatus.ACTIVE);
    }
}
