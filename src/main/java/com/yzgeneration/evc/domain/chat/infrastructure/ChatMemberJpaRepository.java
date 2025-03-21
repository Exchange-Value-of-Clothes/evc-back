package com.yzgeneration.evc.domain.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMemberJpaRepository extends JpaRepository<ChatMemberEntity, Long> {
    Optional<ChatMemberEntity> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
    @Query("SELECT cm FROM ChatMemberEntity cm WHERE cm.chatRoomId = :chatRoomId AND cm.memberId IN :memberIds AND cm.isDeleted = true")
    List<ChatMemberEntity> getDeletedChatMembers(@Param("chatRoomId") Long chatRoomId, @Param("memberIds") List<Long> memberIds);
}
