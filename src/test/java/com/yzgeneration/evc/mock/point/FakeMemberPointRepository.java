package com.yzgeneration.evc.mock.point;

import com.yzgeneration.evc.domain.point.infrastructure.MemberPointRepository;
import com.yzgeneration.evc.domain.point.model.MemberPoint;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;

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

    @Override
    public MemberPoint getById(Long memberId) {
        for (MemberPoint datum : data) {
            if (Objects.equals(datum.getMemberId(), memberId)) return datum;
        }
        throw new CustomException(ErrorCode.POINT_NOT_FOUND, "멤버의 포인트가 존재하지 않습니다.");
    }
}
