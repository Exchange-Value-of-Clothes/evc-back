package com.yzgeneration.evc.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberCreatedEvent {
    private final Long memberId;
}
