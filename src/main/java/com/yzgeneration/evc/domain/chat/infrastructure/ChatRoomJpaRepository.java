package com.yzgeneration.evc.domain.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    @Query("SELECT c FROM ChatRoomEntity c WHERE c.ownerId = :memberId OR c.participationId = :memberId")
    List<ChatRoomEntity> findByOwnerIdOrSenderId(@Param("memberId") Long memberId);
}
