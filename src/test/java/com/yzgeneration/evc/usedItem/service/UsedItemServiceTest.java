package com.yzgeneration.evc.usedItem.service;

import com.yzgeneration.evc.domain.image.implement.UsedItemImageAppender;
import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionStatue;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemAppender;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.mock.usedItem.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;

import static com.yzgeneration.evc.fixture.usedItem.UsedItemFixture.fixCreateUsedItem;
import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = {"cloud.aws.s3.bucket=test-bucket"})
class UsedItemServiceTest {
    private UsedItemService usedItemService;

    @BeforeEach
    void init() throws MalformedURLException {
        UsedItemRepository usedItemRepository = new FakeUsedItemRepository();
        UsedItemImageRepository usedItemImageRepository = new FakeUsedItemImageRepository();

        UsedItemAppender usedItemAppender = new UsedItemAppender(usedItemRepository, new FixedTimeProvider());
        UsedItemImageAppender usedItemImageAppender = new UsedItemImageAppender(usedItemImageRepository, new SpyS3ImageHandler());

        usedItemService = new UsedItemService(usedItemAppender, usedItemImageAppender);
    }

    @Test
    @DisplayName("중고상품 등록이 정상적으로 되는지 체크한다.")
    void createUsedItem() throws IOException {
        //given
        CreateUsedItem createUsedItem = fixCreateUsedItem();

        //when
        UsedItemResponse usedItemResponse = usedItemService.createUsedItem(createUsedItem, new MockUsedItemImageFile().getImageFiles());

        //then
        assertThat(usedItemResponse.getMemberId()).isEqualTo(1L);

        //ItemDetails
        assertThat(usedItemResponse.getItemDetails().getTitle()).isEqualTo(createUsedItem.getCreateItemDetails().getTitle());
        assertThat(usedItemResponse.getItemDetails().getCategory()).isEqualTo(createUsedItem.getCreateItemDetails().getCategory());
        assertThat(usedItemResponse.getItemDetails().getContent()).isEqualTo(createUsedItem.getCreateItemDetails().getContent());
        assertThat(usedItemResponse.getItemDetails().getPrice()).isEqualTo(createUsedItem.getCreateItemDetails().getPrice());

        //UsedItemTransaction
        assertThat(usedItemResponse.getUsedItemTransaction().getTransactionType()).isEqualTo(TransactionType.DIRECT);
        assertThat(usedItemResponse.getUsedItemTransaction().getTransactionMode()).isEqualTo(TransactionMode.BUY);
        assertThat(usedItemResponse.getUsedItemTransaction().getTransactionStatue()).isEqualTo(TransactionStatue.ONGOING);

        //UsedItemStatus & ItemStats
        assertThat(usedItemResponse.getUsedItemStatus()).isEqualTo(UsedItemStatus.ACTIVE);

        assertThat(usedItemResponse.getItemStats().getViewCount()).isEqualTo(0);
        assertThat(usedItemResponse.getItemStats().getLikeCount()).isEqualTo(0);
        assertThat(usedItemResponse.getItemStats().getChattingCount()).isEqualTo(0);

        //ImageURLs & CreateAt
        assertThat(usedItemResponse.getImageURLs()).hasSize(2);
        assertThat(usedItemResponse.getCreateAt()).isEqualTo(LocalDateTime.of(2000, 1, 2, 3, 0, 0, 0));
    }
}
