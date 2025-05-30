package com.yzgeneration.evc.usedItem.model;

import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.yzgeneration.evc.fixture.UsedItemFixture.fixCreateUsedItemRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class UsedItemTest {

    @Test
    @DisplayName("중고상품 model 생성")
    void createUsedItem() {
        //given
        CreateUsedItemRequest createUsedItemRequest = fixCreateUsedItemRequest();

        //when
        UsedItem usedItem = UsedItem.create(1L, createUsedItemRequest, LocalDateTime.now());

        //then
        assertThat(usedItem.getItemDetails().getTitle()).isEqualTo(createUsedItemRequest.getTitle());
        assertThat(usedItem.getUsedItemTransaction().getTransactionType().toString()).isEqualTo(createUsedItemRequest.getTransactionType());
        assertThat(usedItem.getItemStatus()).isEqualTo(ItemStatus.ACTIVE);
    }
}
