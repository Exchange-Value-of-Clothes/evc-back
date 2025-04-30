package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.image.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    Optional<ChatRoomEntity> findByItemIdAndItemTypeAndParticipantId(Long itemId, ItemType itemType, Long participantId);
}
