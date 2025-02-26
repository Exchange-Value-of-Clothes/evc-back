package com.yzgeneration.evc.domain.chat.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yzgeneration.evc.domain.chat.infrastructure.QChatMemberEntity.*;
import static com.yzgeneration.evc.domain.chat.infrastructure.QChatMessageDocument.chatMessageDocument;
import static com.yzgeneration.evc.domain.chat.infrastructure.QChatRoomEntity.chatRoomEntity;


@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ChatMessageMongoRepository chatMessageMongoRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageMongoRepository.save(ChatMessageDocument.from(chatMessage)).toModel();
    }

    @Override
    public List<ChatRoomListResponse> getLastMessages(Long memberId) {
        return jpaQueryFactory.select(
                        Projections.constructor(ChatRoomListResponse.class,
                                chatRoomEntity.id,
                                chatRoomEntity.usedItemId,
                                chatMessageDocument.content,
                                chatMessageDocument.createdAt
                        )
                )
                .from(chatMessageDocument)
                .join(chatRoomEntity).on(chatRoomEntity.id.eq(chatMessageDocument.chatRoomId))
                .join(chatMemberEntity).on(chatRoomEntity.id.eq(chatMemberEntity.chatRoomId))
                .where(
                        chatMemberEntity.memberId.eq(memberId),
                        chatMemberEntity.isDeleted.eq(false)
                )
                .groupBy(chatRoomEntity.id)
                .orderBy(chatMessageDocument.createdAt.max().desc())
                .fetch();
    }

}
