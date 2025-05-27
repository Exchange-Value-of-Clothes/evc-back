package com.yzgeneration.evc.application.event;

import com.yzgeneration.evc.domain.image.infrastructure.repository.ProfileImageRepository;
import com.yzgeneration.evc.domain.image.model.ProfileImage;
import com.yzgeneration.evc.domain.member.MemberCreatedEvent;
import com.yzgeneration.evc.domain.point.infrastructure.MemberPointRepository;
import com.yzgeneration.evc.domain.point.model.MemberPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final MemberPointRepository memberPointRepository;
    private final ProfileImageRepository profileImageRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void initPoint(MemberCreatedEvent event) {
        log.info("initPoint - Transaction active : {}", TransactionSynchronizationManager.isSynchronizationActive());
        Long memberId = event.getMemberId();
        memberPointRepository.save(MemberPoint.create(memberId, 0));
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void initProfileImage(MemberCreatedEvent event) {
        Long memberId = event.getMemberId();
        profileImageRepository.save(ProfileImage.create(memberId, null, event.getImageUrl()));
    }
}
