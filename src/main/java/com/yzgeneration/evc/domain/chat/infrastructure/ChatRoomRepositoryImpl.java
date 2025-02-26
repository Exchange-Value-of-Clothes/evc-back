package com.yzgeneration.evc.domain.chat.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


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
    public ChatRoom getById(Long id) {
        return chatRoomJpaRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND)).toModel();
    }
}
