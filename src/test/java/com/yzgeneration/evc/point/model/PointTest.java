package com.yzgeneration.evc.point.model;

import com.yzgeneration.evc.domain.point.enums.PointChargeStatus;
import com.yzgeneration.evc.domain.point.enums.PointChargeType;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PointTest {
    @Test
    @DisplayName("기존 수량값과 동일한지 검증할 수 있다.")
    void validPoint() {
        // given
        PointCharge pointCharge = PointCharge.create(1L, PointChargeType.PACKAGE_5K);

        // when
        // then
        assertThatThrownBy(() -> pointCharge.validPoint(5900))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("ErrorCode", ErrorCode.INVALID_POINT);
    }

    @Test
    @DisplayName("포인트 충전 상태를 충전됨으로 변경할 수 있다.")
    void confirm() {
        // given
        PointCharge pointCharge = PointCharge.create(1L, PointChargeType.PACKAGE_5K);

        // when
        pointCharge.confirm();

        // then
        assertThat(pointCharge.getPointChargeStatus()).isEqualTo(PointChargeStatus.CHARGED);
    }
}
