package com.yzgeneration.evc.domain.point.infrastructure;

import com.yzgeneration.evc.domain.point.model.MemberPoint;

public interface MemberPointRepository {
    MemberPoint save(MemberPoint memberPoint);
    void charge(Long memberId, int point);
}
