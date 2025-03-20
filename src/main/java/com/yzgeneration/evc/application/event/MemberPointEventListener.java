package com.yzgeneration.evc.application.event;

import com.yzgeneration.evc.domain.member.MemberCreatedEvent;
import com.yzgeneration.evc.domain.point.infrastructure.MemberPointRepository;
import com.yzgeneration.evc.domain.point.model.MemberPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberPointEventListener {

    private final MemberPointRepository memberPointRepository;

    @EventListener
    public void init(MemberCreatedEvent event) {
        Long memberId = event.getMemberId();
        memberPointRepository.save(MemberPoint.create(memberId, 0));
    }
}
