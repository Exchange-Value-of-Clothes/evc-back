package com.yzgeneration.evc.application.event;

import com.yzgeneration.evc.domain.image.infrastructure.repository.ProfileImageRepository;
import com.yzgeneration.evc.domain.image.model.ProfileImage;
import com.yzgeneration.evc.domain.member.MemberCreatedEvent;
import com.yzgeneration.evc.domain.point.infrastructure.MemberPointRepository;
import com.yzgeneration.evc.domain.point.model.MemberPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final MemberPointRepository memberPointRepository;
    private final ProfileImageRepository profileImageRepository;

    @Async
    @EventListener
    public void initPoint(MemberCreatedEvent event) {
        Long memberId = event.getMemberId();
        memberPointRepository.save(MemberPoint.create(memberId, 0));
    }

    @Async
    @EventListener
    public void initProfileImage(MemberCreatedEvent event) {
        Long memberId = event.getMemberId();
        profileImageRepository.save(ProfileImage.of(memberId, null, event.getImageUrl()));
    }
}
