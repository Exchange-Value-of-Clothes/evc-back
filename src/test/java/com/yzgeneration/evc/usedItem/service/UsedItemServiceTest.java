package com.yzgeneration.evc.usedItem.service;

import com.yzgeneration.evc.domain.image.implement.UsedItemImageAppender;
import com.yzgeneration.evc.domain.image.implement.UsedItemImageLoader;
import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.CreateUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsDetails;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsResponse;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionStatue;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemAppender;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemLoader;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.mock.usedItem.FakeUsedItemImageRepository;
import com.yzgeneration.evc.mock.usedItem.FakeUsedItemRepository;
import com.yzgeneration.evc.mock.usedItem.MockUsedItemImageFile;
import com.yzgeneration.evc.mock.usedItem.SpyS3ImageHandler;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.yzgeneration.evc.fixture.usedItem.UsedItemFixture.fixCreateUsedItemRequest;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsedItemServiceTest {
    private UsedItemService usedItemService;

    @BeforeEach
    void init() {
        UsedItemRepository usedItemRepository = new FakeUsedItemRepository();
        UsedItemImageRepository usedItemImageRepository = new FakeUsedItemImageRepository();

        UsedItemAppender usedItemAppender = new UsedItemAppender(usedItemRepository);
        UsedItemLoader usedItemLoader = new UsedItemLoader(usedItemRepository);

        UsedItemImageAppender usedItemImageAppender = new UsedItemImageAppender(usedItemImageRepository, new SpyS3ImageHandler());
        UsedItemImageLoader usedItemImageLoader = new UsedItemImageLoader(usedItemImageRepository);

        usedItemService = new UsedItemService(usedItemAppender, usedItemImageAppender, usedItemLoader, usedItemImageLoader);
    }

    @Test
    @Order(1)
    @DisplayName("중고상품 등록이 정상적으로 되는지 체크")
    void createUsedItem() throws IOException {
        //given
        Long memberId = 1L;
        CreateUsedItemRequest createUsedItemRequest = fixCreateUsedItemRequest();

        //when
        CreateUsedItemResponse createUsedItemResponse = usedItemService.createUsedItem(memberId, createUsedItemRequest, new MockUsedItemImageFile().getImageFiles());

        //then
        assertThat(createUsedItemResponse.getMemberId()).isEqualTo(memberId);
        assertThat(createUsedItemResponse.getUsedItemId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("등록한 중고상품 리스트가 정상적으로 조회되는지 체크")
    void getUsedItems() {
        //given
        int page = 0;

        //when
        LoadUsedItemsResponse loadUsedItemsResponse = usedItemService.loadUsedItems(page);
        LoadUsedItemsDetails loadUsedItemsDetails = loadUsedItemsResponse.getLoadUsedItemDetails().get(0);

        //then
        assertThat(loadUsedItemsDetails.getUsedItemId()).isEqualTo(1L);
        assertThat(loadUsedItemsDetails.getTitle()).isEqualTo("Test UsedItem");
        assertThat(loadUsedItemsDetails.getPrice()).isEqualTo(10000);
        assertThat(loadUsedItemsDetails.getTransactionMode()).isEqualTo(TransactionMode.BUY);
        assertThat(loadUsedItemsDetails.getTransactionStatue()).isEqualTo(TransactionStatue.ONGOING);
        assertThat(loadUsedItemsDetails.getLikeCount()).isEqualTo(0);
        assertThat(loadUsedItemsDetails.getUsedItemStatus()).isEqualTo(UsedItemStatus.ACTIVE);
    }

    @Test
    @DisplayName("등록한 중고상품이 정상적으로 조회되는지 체크")
    void getUsedItem() {
        //given
        Long memberId = 1L;
        Long usedItemId = 1L;

        //when
        LoadUsedItemResponse loadUsedItemResponse = usedItemService.loadUsedItem(memberId, usedItemId);

        //then
        assertThat(loadUsedItemResponse.getTitle()).isEqualTo("Test UsedItem");
        assertThat(loadUsedItemResponse.getCategory()).isEqualTo("top shirt");
        assertThat(loadUsedItemResponse.getContent()).isEqualTo("buy right now");
        assertThat(loadUsedItemResponse.getPrice()).isEqualTo(10000);

        assertThat(loadUsedItemResponse.getTransactionType()).isEqualTo(TransactionType.DIRECT);
        assertThat(loadUsedItemResponse.getTransactionMode()).isEqualTo(TransactionMode.BUY);
        assertThat(loadUsedItemResponse.getTransactionStatue()).isEqualTo(TransactionStatue.ONGOING);

        assertThat(loadUsedItemResponse.getViewCount()).isEqualTo(0);
        assertThat(loadUsedItemResponse.getLikeCount()).isEqualTo(0);
        assertThat(loadUsedItemResponse.getChattingCount()).isEqualTo(0);

        assertThat(loadUsedItemResponse.getMemberId()).isEqualTo(1L);
        assertThat(loadUsedItemResponse.getIsOwned()).isEqualTo(true);
        assertThat(loadUsedItemResponse.getUsedItemStatus()).isEqualTo(UsedItemStatus.ACTIVE);
    }
}
