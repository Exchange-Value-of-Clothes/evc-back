package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    Optional<ChatRoomEntity> findByItemIdAndItemTypeAndParticipantId(Long itemId, ItemType itemType, Long participantId);
}
