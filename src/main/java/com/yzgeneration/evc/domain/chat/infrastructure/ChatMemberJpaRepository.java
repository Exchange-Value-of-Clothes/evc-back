package com.yzgeneration.evc.domain.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberJpaRepository extends JpaRepository<ChatMemberEntity, Long> {
}
