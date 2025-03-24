package com.yzgeneration.evc.domain.point.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPoint {

    private Long memberId;
    private int point;

    public static MemberPoint create(Long memberId, int point) {
        return MemberPoint.builder().memberId(memberId).build();
    }

}
