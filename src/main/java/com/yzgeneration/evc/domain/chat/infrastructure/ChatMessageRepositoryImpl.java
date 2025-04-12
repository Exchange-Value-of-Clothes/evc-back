package com.yzgeneration.evc.domain.chat.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatMessageResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatMessageSliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.yzgeneration.evc.domain.chat.infrastructure.QChatMemberEntity.*;


@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final MongoTemplate mongoTemplate;
    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageMongoRepository.save(ChatMessageDocument.from(chatMessage)).toModel();
    }

    @Override
    public SliceResponse<ChatRoomListResponse> getChatRooms(Long memberId, LocalDateTime cursor) {

        int size=5;

        List<Long> chatRoomIds = jpaQueryFactory.select(chatMemberEntity.chatRoomId)
                .from(chatMemberEntity)
                .where(chatMemberEntity.memberId.eq(memberId)
                        .and(chatMemberEntity.isDeleted.isFalse())) // 탈퇴한 방 제외
                .fetch();

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("chatRoomId").in(chatRoomIds) // 유저가 속한 모든 방 조회
                                .andOperator(
                                        cursor != null ? Criteria.where("createdAt").lt(cursor) : new Criteria()
                                )
                ),
                Aggregation.group("chatRoomId") // 각 채팅방에서 가장 최근 메시지만 가져오기
                        .last("content").as("lastMessage")
                        .last("createdAt").as("createdAt")
                        .last("chatRoomId").as("chatRoomId"),
                Aggregation.sort(Sort.Direction.DESC, "createdAt"), // 최신 메시지부터 정렬
                Aggregation.limit(size + 1) // hasNext 확인을 위해 size+1 만큼 조회
        );

        AggregationResults<ChatRoomListResponse> results = mongoTemplate.aggregate(
                aggregation, "chat_message", ChatRoomListResponse.class
        );
        List<ChatRoomListResponse> response = new ArrayList<>(results.getMappedResults());
        boolean hasNext = response.size() > size;
        if(hasNext) {
            response.remove(size);
        }
        LocalDateTime lastCreatedAt = !response.isEmpty()
                ? response.get(response.size() - 1).getCreatedAt()
                : null;

        return new SliceResponse<>(
                new SliceImpl<>(response, PageRequest.of(0, size), hasNext),
                lastCreatedAt
        );

    }

    @Override
    public ChatMessageSliceResponse getLastMessages(Long memberId, Long chatRoomId, LocalDateTime cursor) {

        int size=10;

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("chatRoomId").is(chatRoomId)
                                .andOperator(cursor != null ? Criteria.where("createdAt").lt(cursor) : new Criteria())
                ),
                Aggregation.sort(Sort.Direction.DESC, "createdAt"),
                Aggregation.limit(size + 1),
                Aggregation.project( "createdAt", "senderId")
                        .and("content").as("message")
                        .andExpression("senderId == " + memberId).as("isMine")
        );

        AggregationResults<ChatMessageResponse> results = mongoTemplate.aggregate(
                aggregation, "chat_message", ChatMessageResponse.class
        );
        List<ChatMessageResponse> response = new ArrayList<>(results.getMappedResults());

        // hasNext 계산을 위해 size + 1 개를 조회했으므로, 초과한 경우 제거하고 hasNext 설정
        boolean hasNext = response.size() > size;
        if (hasNext) {
            response.remove(size);
        }
        LocalDateTime lastCreatedAt = !response.isEmpty()
                ? response.get(response.size() - 1).getCreatedAt()
                : null;

        return new ChatMessageSliceResponse(
                chatRoomId, memberId,new SliceImpl<>(response, PageRequest.of(0, size), hasNext), lastCreatedAt
        );
    }


}
