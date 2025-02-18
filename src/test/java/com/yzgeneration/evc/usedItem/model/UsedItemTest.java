package com.yzgeneration.evc.usedItem.model;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.yzgeneration.evc.fixture.usedItem.UsedItemFixture;
import com.yzgeneration.evc.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.useditem.model.UsedItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UsedItemTest {

    public static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(new FailoverIntrospector(
                    List.of(BuilderArbitraryIntrospector.INSTANCE, FieldReflectionArbitraryIntrospector.INSTANCE)
            ))
            .defaultNotNull(true)
            .build();

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
