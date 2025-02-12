package com.yzgeneration.evc.domain.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    
}
