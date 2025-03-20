package com.yzgeneration.evc.domain.point.implement;

import com.yzgeneration.evc.domain.point.infrastructure.MemberPointRepository;
import com.yzgeneration.evc.domain.point.infrastructure.PointChargeRepository;
import com.yzgeneration.evc.domain.point.model.PointCharge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PointChargeProcessor {

    private final PointChargeRepository pointChargeRepository;
    private final MemberPointRepository memberPointRepository;

    @Transactional
    public void charge(PointCharge pointCharge, Long memberId, int point) {
        memberPointRepository.charge(memberId, point);
        pointCharge.confirm();
        pointChargeRepository.save(pointCharge);
    }
}
