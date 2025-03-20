package com.yzgeneration.evc.mock.point;

import com.yzgeneration.evc.domain.point.infrastructure.MemberPointRepository;
import com.yzgeneration.evc.domain.point.model.MemberPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakeMemberPointRepository implements MemberPointRepository {

    private final List<MemberPoint> data = new ArrayList<>();

    @Override
    public MemberPoint save(MemberPoint memberPoint) {
        data.removeIf(item -> Objects.equals(item.getMemberId(), memberPoint.getMemberId()));
        data.add(memberPoint);
        return memberPoint;
    }

    @Override
    public void charge(Long memberId, int point) {
        for (MemberPoint memberPoint : data) {
            if (memberPoint.getMemberId().equals(memberId)) {
                int updatedPoint = memberPoint.getPoint() + point;
                data.remove(memberPoint);
                data.add(MemberPoint.create(memberId, updatedPoint));
                return;
            }
        }
    }
}
