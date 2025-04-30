package com.yzgeneration.evc.domain.chat.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ChatRoomJpaRepository chatRoomJpaRepository;


    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomJpaRepository.save(ChatRoomEntity.from(chatRoom)).toModel();
    }

    @Override
    public Optional<ChatRoom> findByItemIdAndItemTypeAndParticipantId(Long itemId, ItemType itemType, Long participantId) {
        return chatRoomJpaRepository.findByItemIdAndItemTypeAndParticipantId(itemId, itemType, participantId)
                .map(ChatRoomEntity::toModel);
    }

}
