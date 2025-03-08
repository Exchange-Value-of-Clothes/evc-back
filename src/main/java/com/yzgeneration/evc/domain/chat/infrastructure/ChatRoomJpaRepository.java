package com.yzgeneration.evc.domain.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    @Query("SELECT c FROM ChatRoomEntity c WHERE c.ownerId = :memberId OR c.participantId = :memberId")
    List<ChatRoomEntity> findByOwnerIdOrSenderId(@Param("memberId") Long memberId);

    Optional<ChatRoomEntity> findByUsedItemIdAndParticipantId(Long itemId, Long participantId);
}
