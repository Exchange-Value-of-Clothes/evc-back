package com.yzgeneration.evc.delivery.service;

import com.yzgeneration.evc.domain.delivery.impl.DeliveryProcessor;
import com.yzgeneration.evc.domain.delivery.service.DeliveryService;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.external.delivery.GetKakaoMobilityOrder;
import com.yzgeneration.evc.external.delivery.KakaoMobilityOrderResponse;
import com.yzgeneration.evc.mock.delivery.SpyDeliveryRepository;
import com.yzgeneration.evc.mock.delivery.StubDeliveryViewRepository;
import com.yzgeneration.evc.mock.delivery.StubMobility;
import com.yzgeneration.evc.mock.image.StubProfileImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class DeliveryServiceTest {

    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryService(new DeliveryProcessor(new SpyDeliveryRepository(), new StubDeliveryViewRepository()), new StubMobility(), new StubProfileImageRepository());
    }

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void order() {
        // given
        ItemType itemType = ItemType.AUCTIONITEM;
        Long itemId = 1L;
        Long buyerId = 1L;
        Long sellerId = 2L;
        Long memberId = 2L;
        // when then
        KakaoMobilityOrderResponse response = deliveryService.order(itemType, itemId, buyerId, sellerId, memberId);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("주문은 오직 판매자만 생성할 수 있다.")
    void order_fail() {
        // given
        ItemType itemType = ItemType.AUCTIONITEM;
        Long itemId = 1L;
        Long buyerId = 1L;
        Long sellerId = 2L;
        Long memberId = 1L;
        // when
        // then
        assertThatThrownBy(()->deliveryService.order(itemType, itemId, buyerId, sellerId, memberId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DELIVERY_ONLY_SELLER_REQUEST);

    }

    @Test
    @DisplayName("주문을 조회할 수 있다.")
    void get() {
        // given
        ItemType itemType = ItemType.AUCTIONITEM;
        Long itemId = 1L;
        Long buyerId = 1L;
        Long sellerId = 2L;
        Long memberId = 2L;
        KakaoMobilityOrderResponse response = deliveryService.order(itemType, itemId, buyerId, sellerId, memberId);

        // when then
        GetKakaoMobilityOrder getOrder = deliveryService.get(response.getReceipt().getOrderId(), memberId);
        assertThat(getOrder).isNotNull();
    }

    @Test
    @DisplayName("주문 연관자들만 조회할 수 있다.")
    void get_only_related() {
        // given
        ItemType itemType = ItemType.AUCTIONITEM;
        Long itemId = 1L;
        Long buyerId = 1L;
        Long sellerId = 2L;
        Long memberId = 2L;
        KakaoMobilityOrderResponse response = deliveryService.order(itemType, itemId, buyerId, sellerId, memberId);

        // when then
        assertThatThrownBy(()->deliveryService.get(response.getReceipt().getOrderId(), 3L))
        .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DELIVERY_NOT_FOUND);
    }


}
