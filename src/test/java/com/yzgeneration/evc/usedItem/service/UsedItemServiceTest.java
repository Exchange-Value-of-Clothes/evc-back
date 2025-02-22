package com.yzgeneration.evc.usedItem.service;

import com.yzgeneration.evc.domain.image.implement.UsedItemImageAppender;
import com.yzgeneration.evc.domain.image.implement.UsedItemImageLoader;
import com.yzgeneration.evc.domain.image.service.port.UsedItemImageRepository;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.CreateUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemAppender;
import com.yzgeneration.evc.domain.useditem.implement.UsedItemLoader;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.mock.usedItem.FakeUsedItemImageRepository;
import com.yzgeneration.evc.mock.usedItem.FakeUsedItemRepository;
import com.yzgeneration.evc.mock.usedItem.MockUsedItemImageFile;
import com.yzgeneration.evc.mock.usedItem.SpyS3ImageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static com.yzgeneration.evc.fixture.usedItem.UsedItemFixture.fixCreateUsedItemRequest;
import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = {"cloud.aws.s3.bucket=test-bucket"})
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
    @DisplayName("중고상품 등록이 정상적으로 되는지 체크한다.")
    void createUsedItem() throws IOException {
        //given
        CreateUsedItemRequest createUsedItemRequest = fixCreateUsedItemRequest();

        //when
        CreateUsedItemResponse createUsedItemResponse = usedItemService.createUsedItem(createUsedItemRequest, new MockUsedItemImageFile().getImageFiles());

        //then
        assertThat(createUsedItemResponse.getMemberId()).isEqualTo(1L);
        assertThat(createUsedItemResponse.getUsedItemId()).isEqualTo(1L);

    }
}
