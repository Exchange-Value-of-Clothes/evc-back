package com.yzgeneration.evc.domain.item.useditem.infrastructure.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemListResponse.GetUsedItemListResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
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
import static com.yzgeneration.evc.domain.item.useditem.infrastructure.entity.QUsedItemEntity.usedItemEntity;
import static com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity.memberEntity;

@Repository
@RequiredArgsConstructor
public class UsedItemRepositoryImpl implements UsedItemRepository {
    private final UsedItemJpaRepository usedItemJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private static final int SIZE = 10;

    @Override
    public UsedItem save(UsedItem usedItem) {
        return usedItemJpaRepository.save(UsedItemEntity.from(usedItem)).toModel();
    }

    @Override
    public SliceResponse<GetUsedItemListResponse> getUsedItemList(LocalDateTime cursor) {

        List<GetUsedItemListResponse> usedItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetUsedItemListResponse.class,
                        usedItemEntity.id,
                        usedItemEntity.itemDetailsEntity.title,
                        usedItemEntity.itemDetailsEntity.price,
                        usedItemEntity.usedItemTransactionEntity.transactionMode,
                        usedItemEntity.usedItemTransactionEntity.transactionStatus,
                        itemImageEntity.imageName,
                        //TODO like 테이블 생기면 join해서 해당 값 채우기
                        usedItemEntity.itemStatsEntity.likeCount,
                        usedItemEntity.createdAt,
                        usedItemEntity.itemStatus)
                )
                .from(usedItemEntity)
                .join(itemImageEntity)
                .on(itemImageEntity.itemId.eq(usedItemEntity.id), itemImageEntity.isThumbnail)
                .where(usedItemEntity.itemStatus.eq(ItemStatus.ACTIVE)
                        //TODO(판매완료된 거는 빼고 줄까?)
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
    public Optional<GetUsedItemResponse> findByMemberIdAndUsedItemId(Long memberId, Long usedItemId) {

        GetUsedItemResponse usedItemResponse = jpaQueryFactory
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
                        usedItemEntity.itemStatsEntity.likeCount,
                        usedItemEntity.itemStatsEntity.chattingCount,
                        usedItemEntity.memberId,
                        memberEntity.memberPrivateInformationEntity.nickname,
                        //조회한 중고상품의 게시자와 조회자의 일치 체크
                        usedItemEntity.memberId.eq(memberId),
                        usedItemEntity.createdAt,
                        usedItemEntity.itemStatus
                ))
                .from(usedItemEntity)
                .join(memberEntity)
                .on(memberEntity.id.eq(usedItemEntity.memberId))
                .where(usedItemEntity.id.eq(usedItemId))
                .fetchFirst();

        return Optional.ofNullable(usedItemResponse);
    }

    @Override
    public UsedItem getById(Long id) {
        return usedItemJpaRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USEDITEM_NOT_FOUND)).toModel();
    }

    @Override
    public SliceResponse<GetUsedItemListResponse> searchUsedItemList(String keyword, LocalDateTime cursor) {

        List<GetUsedItemListResponse> usedItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetUsedItemListResponse.class,
                        usedItemEntity.id,
                        usedItemEntity.itemDetailsEntity.title,
                        usedItemEntity.itemDetailsEntity.price,
                        usedItemEntity.usedItemTransactionEntity.transactionMode,
                        usedItemEntity.usedItemTransactionEntity.transactionStatus,
                        itemImageEntity.imageName,
                        //TODO like 테이블 생기면 join해서 해당 값 채우기
                        usedItemEntity.itemStatsEntity.likeCount,
                        usedItemEntity.createdAt,
                        usedItemEntity.itemStatus)
                )
                .from(usedItemEntity)
                .join(itemImageEntity)
                .on(itemImageEntity.itemId.eq(usedItemEntity.id), itemImageEntity.isThumbnail)
                .where(usedItemEntity.itemStatus.eq(ItemStatus.ACTIVE)
                        //TODO(판매완료된 거는 빼고 줄까?)
                        .and(cursor != null ? usedItemEntity.createdAt.lt(cursor) : null)
                        .and(usedItemEntity.itemDetailsEntity.title.containsIgnoreCase(keyword))
                        .or(usedItemEntity.itemDetailsEntity.content.containsIgnoreCase(keyword)))
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
}
