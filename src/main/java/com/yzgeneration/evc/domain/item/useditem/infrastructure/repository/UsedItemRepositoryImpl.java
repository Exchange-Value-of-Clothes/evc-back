package com.yzgeneration.evc.domain.item.useditem.infrastructure.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetMyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.infrastructure.entity.UsedItemEntity;
import com.yzgeneration.evc.domain.item.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.item.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.yzgeneration.evc.domain.image.infrastructure.entity.QItemImageEntity.itemImageEntity;
import static com.yzgeneration.evc.domain.image.infrastructure.entity.QProfileImageEntity.profileImageEntity;
import static com.yzgeneration.evc.domain.item.useditem.infrastructure.entity.QUsedItemEntity.usedItemEntity;
import static com.yzgeneration.evc.domain.like.infrastructure.entity.QLikeEntity.likeEntity;
import static com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity.memberEntity;

@Repository
@RequiredArgsConstructor
public class UsedItemRepositoryImpl implements UsedItemRepository {
    private final UsedItemJpaRepository usedItemJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private static final int SIZE = 10;
    private static final ItemType ITEM_TYPE = ItemType.USEDITEM;

    @Override
    public UsedItem save(UsedItem usedItem) {
        return usedItemJpaRepository.save(UsedItemEntity.from(usedItem)).toModel();
    }

    @Override
    public SliceResponse<GetUsedItemsResponse> getUsedItems(LocalDateTime cursor, Long memberId) {

        List<GetUsedItemsResponse> usedItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetUsedItemsResponse.class,
                        usedItemEntity.id,
                        usedItemEntity.itemDetailsEntity.title,
                        usedItemEntity.itemDetailsEntity.price,
                        usedItemEntity.usedItemTransactionEntity.transactionMode,
                        usedItemEntity.usedItemTransactionEntity.transactionStatus,
                        itemImageEntity.imageName,
                        Expressions.constant(0L),
                        usedItemEntity.createdAt,
                        usedItemEntity.itemStatus,
                        likeEntity.id.isNotNull())
                )
                .from(usedItemEntity)
                .leftJoin(itemImageEntity)
                .on(itemImageEntity.itemId.eq(usedItemEntity.id)
                        .and(itemImageEntity.isThumbnail.isTrue())
                        .and(itemImageEntity.itemType.eq(ITEM_TYPE)))
                .leftJoin(likeEntity)
                .on(likeEntity.itemId.eq(usedItemEntity.id)
                        .and(likeEntity.itemType.eq(ITEM_TYPE))
                        .and(likeEntity.memberId.eq(memberId)))
                .where(usedItemEntity.itemStatus.eq(ItemStatus.ACTIVE)
                        .and(cursor != null ? usedItemEntity.createdAt.lt(cursor) : null))
                .orderBy(usedItemEntity.createdAt.desc())
                .limit(SIZE + 1)
                .fetch();

        boolean hasNext = usedItemListResponses.size() > SIZE; //true: 조회할 상품이 더 남은 상태 (조회 결과 : 11개)
        if (hasNext) {
            usedItemListResponses.remove(SIZE);
        }

        LocalDateTime localCreateTime = !usedItemListResponses.isEmpty() ? usedItemListResponses.get(usedItemListResponses.size() - 1).getCreateAt() : null;

        return new SliceResponse<>(
                new SliceImpl<>(usedItemListResponses, PageRequest.of(0, SIZE), hasNext), localCreateTime
        );
    }

    @Override
    public SliceResponse<GetMyOrMemberUsedItemsResponse> getMyOrMemberUsedItems(Long memberId, LocalDateTime cursor, TransactionMode transactionMode) {

        List<GetMyOrMemberUsedItemsResponse> memberUsedItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetMyOrMemberUsedItemsResponse.class,
                        usedItemEntity.id,
                        usedItemEntity.itemDetailsEntity.title,
                        usedItemEntity.itemDetailsEntity.price,
                        usedItemEntity.usedItemTransactionEntity.transactionMode,
                        usedItemEntity.usedItemTransactionEntity.transactionStatus,
                        itemImageEntity.imageName,
                        Expressions.constant(0L),
                        usedItemEntity.createdAt,
                        usedItemEntity.itemStatus)
                )
                .from(usedItemEntity)
                .leftJoin(itemImageEntity)
                .on(itemImageEntity.itemId.eq(usedItemEntity.id)
                        .and(itemImageEntity.isThumbnail.isTrue())
                        .and(itemImageEntity.itemType.eq(ITEM_TYPE))
                )
                .where(usedItemEntity.itemStatus.eq(ItemStatus.ACTIVE)
                        //TODO(판매완료된 거는 빼고 줄까?)
                        .and(usedItemEntity.memberId.eq(memberId)) //본인이 게시한 거 조회
                        .and(createFilterbuilder(transactionMode)) //SELL, BUY, ALL 조회 필터링
                        .and(cursor != null ? usedItemEntity.createdAt.lt(cursor) : null))
                .orderBy(usedItemEntity.createdAt.desc())
                .limit(SIZE + 1)
                .fetch();

        boolean hasNext = memberUsedItemListResponses.size() > SIZE; //true: 조회할 상품이 더 남은 상태 (조회 결과 : 11개)
        if (hasNext) {
            memberUsedItemListResponses.remove(SIZE);
        }

        LocalDateTime localCreateTime = !memberUsedItemListResponses.isEmpty() ? memberUsedItemListResponses.get(memberUsedItemListResponses.size() - 1).getCreateAt() : null;

        return new SliceResponse<>(
                new SliceImpl<>(memberUsedItemListResponses, PageRequest.of(0, SIZE), hasNext), localCreateTime
        );
    }

    @Override
    public GetUsedItemResponse findUsedItemByMemberIdAndUsedItemId(Long memberId, Long usedItemId) {
        return Optional.ofNullable(jpaQueryFactory
                        .select(Projections.constructor(GetUsedItemResponse.class,
                                usedItemEntity.itemDetailsEntity.title,
                                usedItemEntity.itemDetailsEntity.category,
                                usedItemEntity.itemDetailsEntity.content,
                                usedItemEntity.itemDetailsEntity.price,
                                usedItemEntity.usedItemTransactionEntity.transactionType,
                                usedItemEntity.usedItemTransactionEntity.transactionMode,
                                usedItemEntity.usedItemTransactionEntity.transactionStatus,
                                Expressions.constant(new ArrayList<>()),
                                usedItemEntity.itemStatsEntity.viewCount,
                                Expressions.constant(0L),
                                usedItemEntity.itemStatsEntity.chattingCount,
                                usedItemEntity.memberId,
                                memberEntity.memberPrivateInformationEntity.nickname,
                                profileImageEntity.name,
                                //조회한 중고상품의 게시자와 조회자의 일치 체크
                                usedItemEntity.memberId.eq(memberId),
                                usedItemEntity.createdAt,
                                usedItemEntity.itemStatus
                        ))
                        .from(usedItemEntity)
                        .join(memberEntity)
                        .on(memberEntity.id.eq(usedItemEntity.memberId))
                        .join(profileImageEntity)
                        .on(profileImageEntity.memberId.eq(usedItemEntity.memberId))
                        .where(usedItemEntity.id.eq(usedItemId)
                                .and(usedItemEntity.itemStatus.eq(ItemStatus.ACTIVE)))
                        .fetchFirst())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.USEDITEM_NOT_FOUND)
                );
    }

    @Override
    public UsedItem getById(Long id) {
        return usedItemJpaRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USEDITEM_NOT_FOUND)).toModel();
    }

    @Override
    public SliceResponse<GetUsedItemsResponse> searchUsedItems(String q, LocalDateTime cursor, Long memberId) {

        List<GetUsedItemsResponse> usedItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetUsedItemsResponse.class,
                        usedItemEntity.id,
                        usedItemEntity.itemDetailsEntity.title,
                        usedItemEntity.itemDetailsEntity.price,
                        usedItemEntity.usedItemTransactionEntity.transactionMode,
                        usedItemEntity.usedItemTransactionEntity.transactionStatus,
                        itemImageEntity.imageName,
                        //TODO like 테이블 생기면 join해서 해당 값 채우기
                        Expressions.constant(0L),
                        usedItemEntity.createdAt,
                        usedItemEntity.itemStatus,
                        likeEntity.id.isNotNull())
                )
                .from(usedItemEntity)
                .leftJoin(itemImageEntity)
                .on(itemImageEntity.itemId.eq(usedItemEntity.id)
                        .and(itemImageEntity.isThumbnail.isTrue())
                        .and(itemImageEntity.itemType.eq(ITEM_TYPE))
                )
                .leftJoin(likeEntity)
                .on(likeEntity.itemId.eq(usedItemEntity.id)
                        .and(likeEntity.itemType.eq(ITEM_TYPE))
                        .and(likeEntity.memberId.eq(memberId)))
                .where(usedItemEntity.itemStatus.eq(ItemStatus.ACTIVE)
                        //TODO(판매완료된 거는 빼고 줄까?)
                        .and(cursor != null ? usedItemEntity.createdAt.lt(cursor) : null)
                        .and(usedItemEntity.itemDetailsEntity.title.containsIgnoreCase(q))
                        .or(usedItemEntity.itemDetailsEntity.content.containsIgnoreCase(q)))
                .orderBy(usedItemEntity.createdAt.desc())
                .limit(SIZE + 1)
                .fetch();

        boolean hasNext = usedItemListResponses.size() > SIZE; //true: 조회할 상품이 더 남은 상태 (조회 결과 : 11개)
        if (hasNext) {
            usedItemListResponses.remove(SIZE);
        }

        LocalDateTime localCreateTime = !usedItemListResponses.isEmpty() ? usedItemListResponses.get(usedItemListResponses.size() - 1).getCreateAt() : null;

        return new SliceResponse<>(
                new SliceImpl<>(usedItemListResponses, PageRequest.of(0, SIZE), hasNext), localCreateTime
        );
    }

    @Override
    public Long countUsedItemByMemberId(Long memberId) {
        return jpaQueryFactory.select(usedItemEntity.count())
                .from(usedItemEntity)
                .where(usedItemEntity.memberId.eq(memberId)
                        .and(usedItemEntity.itemStatus.eq(ItemStatus.ACTIVE)))
                .fetchOne();
    }

    private BooleanBuilder createFilterbuilder(TransactionMode condition) {
        BooleanBuilder filterBuilder = new BooleanBuilder();
        return (condition == TransactionMode.SELL || condition == TransactionMode.BUY) ? filterBuilder.and(usedItemEntity.usedItemTransactionEntity.transactionMode.eq(condition)) : filterBuilder;
    }
}
