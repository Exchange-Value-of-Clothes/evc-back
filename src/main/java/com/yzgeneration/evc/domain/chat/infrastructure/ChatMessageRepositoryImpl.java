package com.yzgeneration.evc.domain.chat.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.*;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.infrastructure.entity.QProfileImageEntity;

import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.QAuctionItemEntity;
import com.yzgeneration.evc.domain.item.useditem.infrastructure.entity.QUsedItemEntity;
import com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity;
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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yzgeneration.evc.domain.image.infrastructure.entity.QProfileImageEntity.profileImageEntity;
import static com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.QAuctionItemEntity.auctionItemEntity;
import static com.yzgeneration.evc.domain.item.useditem.infrastructure.entity.QUsedItemEntity.*;


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
        // 1. chatRoomId + 상대방 id + 닉네임 + 이미지 한 번에 조회
        QChatMemberEntity chatMember = new QChatMemberEntity("chatMember");
        QChatRoomEntity chatRoom = new QChatRoomEntity("chatRoom");
        QMemberEntity owner = new QMemberEntity("owner");
        QMemberEntity participant = new QMemberEntity("participant");
        QProfileImageEntity ownerProfile = new QProfileImageEntity("ownerProfile");
        QProfileImageEntity participantProfile = new QProfileImageEntity("participantProfile");

        List<ChatRoomListMetadata> chatRoomInfoList = jpaQueryFactory
                .select(Projections.constructor(
                        ChatRoomListMetadata.class,
                        chatRoom.id,
                        // 상대방 ID
                        Expressions.numberTemplate(Long.class,
                                "CASE WHEN {0} = {1} THEN {2} ELSE {1} END",
                                chatRoom.ownerId, memberId, chatRoom.participantId),
                        // 상대방 닉네임
                        Expressions.stringTemplate(
                                "CASE WHEN {0} = {1} THEN {2} ELSE {3} END",
                                chatRoom.ownerId, memberId,
                                participant.memberPrivateInformationEntity.nickname,
                                owner.memberPrivateInformationEntity.nickname),
                        // 상대방 프로필 이미지
                        Expressions.stringTemplate(
                                "CASE WHEN {0} = {1} THEN {2} ELSE {3} END",
                                chatRoom.ownerId, memberId,
                                participantProfile.imageUrl,
                                ownerProfile.imageUrl)
                ))
                .from(chatMember)
                .join(chatRoom).on(chatMember.chatRoomId.eq(chatRoom.id))
                .join(owner).on(chatRoom.ownerId.eq(owner.id))
                .join(participant).on(chatRoom.participantId.eq(participant.id))
                .leftJoin(ownerProfile).on(owner.id.eq(ownerProfile.memberId))
                .leftJoin(participantProfile).on(participant.id.eq(participantProfile.memberId))
                .where(chatMember.memberId.eq(memberId)
                        .and(chatMember.isDeleted.isFalse()))
                .fetch();

        // 2. Map으로 chatRoomId -> Metadata 매핑 (몽고디비 메시지와 병합처리 위해)
        Map<Long, ChatRoomListMetadata> chatRoomInfoMap = chatRoomInfoList.stream()
                .collect(Collectors.toMap(ChatRoomListMetadata::getChatRoomId, Function.identity()));

        // 3. 최근 메시지 MongoDB에서 Aggregation 조회
        List<Long> chatRoomIds = chatRoomInfoList.stream()
                .map(ChatRoomListMetadata::getChatRoomId)
                .collect(Collectors.toList());

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

        // 4. 상대방 정보 매핑
        for (ChatRoomListResponse dto : response) {
            ChatRoomListMetadata meta = chatRoomInfoMap.get(dto.getChatRoomId());
            if (meta != null) {
                dto.setOtherMemberId(meta.getOtherMemberId());
                dto.setOtherNickname(meta.getNickname());
                dto.setProfileImageUrl(meta.getProfileImageUrl());
            }
        }

        // 5. 페이징 처리
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
    public ChatMessageSliceResponse getLastMessages(Long memberId, Long chatRoomId, LocalDateTime cursor, Long ownerId,
                                                    ItemType itemType, Long itemId, Long otherPersonId) {

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

        ChatRoomMetaData chatRoomMetaData;

        if (itemType == ItemType.USEDITEM) {
            chatRoomMetaData = jpaQueryFactory.select(Projections.constructor(
                    ChatRoomMetaData.class,
                    usedItemEntity.usedItemTransactionEntity.transactionType,
                    usedItemEntity.itemDetailsEntity.title,
                    usedItemEntity.itemDetailsEntity.price,
                    profileImageEntity.imageUrl
                    )).from(usedItemEntity)
                    .where(usedItemEntity.id.eq(itemId))
                    .join(profileImageEntity).on(profileImageEntity.memberId.eq(otherPersonId))
                    .fetchFirst();
        } else {
            chatRoomMetaData = jpaQueryFactory.select(Projections.constructor(
                            ChatRoomMetaData.class,
                            auctionItemEntity.transactionType,
                            auctionItemEntity.auctionItemDetailsEntity.title,
                            auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice,
                            profileImageEntity.imageUrl
                    )).from(auctionItemEntity)
                    .where(auctionItemEntity.id.eq(itemId))
                    .join(profileImageEntity).on(profileImageEntity.memberId.eq(otherPersonId))
                    .fetchFirst();
        }

        return new ChatMessageSliceResponse(
                chatRoomId, memberId, ownerId, new SliceImpl<>(response, PageRequest.of(0, size), hasNext), lastCreatedAt, otherPersonId,
        itemType, itemId, Objects.requireNonNull(chatRoomMetaData).getTransactionType().name(), chatRoomMetaData.getTitle(), chatRoomMetaData.getPrice(), chatRoomMetaData.getOtherPersonProfileUrl());
    }


}
