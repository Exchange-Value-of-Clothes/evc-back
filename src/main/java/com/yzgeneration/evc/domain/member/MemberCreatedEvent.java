package com.yzgeneration.evc.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberCreatedEvent {
    private Long memberId;
    private String imageUrl;

    public MemberCreatedEvent(Long memberId, String imageUrl) {
        this.memberId = memberId;
        this.imageUrl = imageUrl;
    }
}
