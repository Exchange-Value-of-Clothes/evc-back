package com.yzgeneration.evc.point.service;

import com.yzgeneration.evc.domain.point.enums.PointChargeStatus;
import com.yzgeneration.evc.domain.point.enums.PointChargeType;
import com.yzgeneration.evc.domain.point.implement.PointChargeProcessor;
import com.yzgeneration.evc.domain.point.implement.PointChargeValidator;
import com.yzgeneration.evc.domain.point.infrastructure.PointChargeRepository;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import com.yzgeneration.evc.domain.point.service.PointChargeService;
import com.yzgeneration.evc.fixture.PointChargeFixture;
import com.yzgeneration.evc.mock.external.SpyPaymentGateway;
import com.yzgeneration.evc.mock.point.FakeMemberPointRepository;
import com.yzgeneration.evc.mock.point.FakePointChargeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PointChargeServiceTest {

    private PointChargeService pointChargeService;
    private PointChargeRepository pointChargeRepository;

    @BeforeEach
    void init() {

        pointChargeRepository = new FakePointChargeRepository();

        pointChargeService = new PointChargeService(
                pointChargeRepository,
                new PointChargeValidator(pointChargeRepository),
                new SpyPaymentGateway(),
                new PointChargeProcessor(pointChargeRepository, new FakeMemberPointRepository())
        );
    }

    @Test
    @DisplayName("포인트 충전 주문을 생성할 수 있다.")
    void createPointCharge() {
        // given
        PointCharge pointCharge = PointCharge.create(1L, PointChargeType.PACKAGE_5K);

        // when
        PointCharge newPointCharge = pointChargeService.createOrder(pointCharge);

        // then
        assertThat(newPointCharge.getPointChargeStatus()).isEqualTo(PointChargeStatus.ORDERED);
        assertThat(newPointCharge.getPointChargeType()).isEqualTo(PointChargeType.PACKAGE_5K);
        assertThat(newPointCharge.getMemberId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("결제 승인을 할 수 있다.")
    void confirm() {
        // given
        String orderId = "orderId";
        String paymentKey = "paymentKey";
        int amount = 5000;
        Long memberId = 1L;
        PointCharge pointCharge = PointChargeFixture.createPointCharge(orderId, memberId, PointChargeType.PACKAGE_5K);
        pointChargeService.createOrder(pointCharge);

        // when
        pointChargeService.confirm(orderId, paymentKey, amount, memberId);

        // then
        PointCharge savedPointCharge = pointChargeRepository.getById(orderId);
        assertThat(savedPointCharge.getMemberId()).isEqualTo(1L);
        assertThat(savedPointCharge.getPointChargeType()).isEqualTo(PointChargeType.PACKAGE_5K);
        assertThat(savedPointCharge.getPointChargeStatus()).isEqualTo(PointChargeStatus.CHARGED);
        assertThat(savedPointCharge.getPointChargeStatus()).isEqualTo(PointChargeStatus.CHARGED);

    }
}
