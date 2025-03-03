package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.common.dto.SliceResponse;
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
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final MongoTemplate mongoTemplate;
    private final ChatMessageMongoRepository chatMessageMongoRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageMongoRepository.save(ChatMessageDocument.from(chatMessage)).toModel();
    }

    @Override
    public SliceResponse<ChatRoomListResponse> getLastMessages(Long memberId) {
        int size=10; // TODO argument
        LocalDateTime cursor = LocalDateTime.now();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("participationId").is(memberId)
                                .andOperator(
                                        cursor != null
                                                ? Criteria.where("createdAt").lt(cursor) // Cursor 방식으로 이후 데이터만 조회
                                                : new Criteria() // 첫 페이지일 경우 조건 없음
                                ) // Cursor 방식으로 이후 데이터만 조회
                ),
                Aggregation.group("chatRoomId")
                        .last("content").as("lastMessage")
                        .last("createdAt").as("createdAt")
                        .last("chatRoomId").as("chatRoomId"),
                Aggregation.sort(Sort.Direction.DESC, "createdAt"),
                Aggregation.limit(size+1) // hasNext 확인
        );

        AggregationResults<ChatRoomListResponse> results = mongoTemplate.aggregate(
                aggregation, "chat_message", ChatRoomListResponse.class
        );
        List<ChatRoomListResponse> response = results.getMappedResults();
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
        // Slice기본구현체에는 쓸때없는 정보가 너무많고
        //https://wimoney.tistory.com/entry/SpringDataJPA-%ED%9A%A8%EC%9C%A8%EC%A0%81%EC%9D%B8-%ED%8E%98%EC%9D%B4%EC%A7%95%EC%9D%84-%EC%9C%84%ED%95%9C-Pageable-Slice-%EB%B6%84%EC%84%9D
        // PageRequest에서 없는 페이지(getHasContent:false)라 나타나도 Slice.isLast는 true값을 리턴해줘서 오해의 소지를 불러올 수 있음 -> 검증로직 처리해줘야함

    }


}
