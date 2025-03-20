package com.yzgeneration.evc.domain.point.infrastructure;

import com.yzgeneration.evc.domain.point.model.MemberPoint;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Entity
@Builder
@Table(name = "member_points")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberPointEntity {

    @Id
    private Long memberId;
    private int point;

    public static MemberPointEntity from(MemberPoint memberPoint) {
        return MemberPointEntity.builder()
                .memberId(memberPoint.getMemberId())
                .point(memberPoint.getPoint())
                .build();
    }

    public MemberPoint toModel() {
        return MemberPoint.builder()
                .memberId(memberId)
                .point(point).build();
    }
}
