package com.yzgeneration.evc.usedItem.model;

import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import com.yzgeneration.evc.fixture.usedItem.UsedItemFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UsedItemTest {

    @Test
    @DisplayName("중고상품 model 생성")
    void createUsedItem() {
        //given
        CreateUsedItem createUsedItem = UsedItemFixture.fixCreateUsedItem();

        //when
        UsedItem usedItem = UsedItem.create(createUsedItem, LocalDateTime.now());

        //then
        assertThat(usedItem.getItemDetails().getTitle()).isEqualTo(createUsedItem.getCreateItemDetails().getTitle());
        assertThat(usedItem.getUsedItemTransaction().getTransactionType()).isEqualTo(createUsedItem.getCreateTransaction().getTransactionType());
        assertThat(usedItem.getUsedItemStatus()).isEqualTo(UsedItemStatus.ACTIVE);
    }

//    @Test
//    @DisplayName("중고상품 생성시, 빈값을 넣으면 예외 발생")
//    void createUsedItemWithEmptyValues() {
//        //given
//        CreateUsedItem createUsedItem = fixtureMonkey.giveMeBuilder(CreateUsedItem.class)
//                .set("memberId", 1L)
//                .set("createItemDetails", fixtureMonkey.giveMeBuilder(CreateItemDetails.class)
//                        .set("title", "")
//                        .set("category", "")
//                        .set("content", "강매는 아닌데 일단 사십쇼")
//                        .set("price", 10000)
//                        .sample())
//                .set("createTransaction", UsedItemFixture.fixCreateTransaction())
//                .sample();
//
//        //when
//
//
//        //then
//
//    }
}
