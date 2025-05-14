package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemStatsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.AuctionItemPriceDetailResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.GetAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.GetMyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionParticipateResponse;
import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.AuctionItemEntity;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionRoomRepository;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.yzgeneration.evc.domain.image.infrastructure.entity.QItemImageEntity.itemImageEntity;
import static com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.QAuctionItemEntity.auctionItemEntity;
import static com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity.memberEntity;
import static com.yzgeneration.evc.domain.point.infrastructure.QMemberPointEntity.memberPointEntity;

@Repository
@RequiredArgsConstructor
public class AuctionItemRepositoryImpl implements AuctionItemRepository {
    private final AuctionItemJpaRepository auctionItemJpaRepository;
    private final AuctionRoomRepository auctionRoomRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final MongoTemplate mongoTemplate;
    private static final int SIZE = 10;
    private static final ItemType ITEM_TYPE = ItemType.AUCTIONITEM;

    @Override
    public AuctionItem save(AuctionItem auctionItem) {
        return auctionItemJpaRepository.save(AuctionItemEntity.from(auctionItem)).toModel();
    }

    @Override
    public SliceResponse<GetAuctionItemsResponse> getAuctionItems(Long memberId, LocalDateTime cursor) {

        List<GetAuctionItemsResponse> auctionItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetAuctionItemsResponse.class,
                        auctionItemEntity.id,
                        auctionItemEntity.auctionItemDetailsEntity.title,
                        auctionItemEntity.auctionItemDetailsEntity.category,
                        Projections.constructor(AuctionItemPriceDetailResponse.class,
                                auctionItemEntity.auctionItemPriceDetailsEntity.startPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.bidPrice),
                        Expressions.constant(0L),
                        itemImageEntity.imageName,
                        auctionItemEntity.startTime,
                        auctionItemEntity.endTime,
                        memberPointEntity.point))
                .from(auctionItemEntity)
                .join(itemImageEntity) //썸네일 조회를 위해 join
                .on(itemImageEntity.itemId.eq(auctionItemEntity.id)
                        .and(itemImageEntity.isThumbnail.isTrue())
                        .and(itemImageEntity.itemType.eq(ITEM_TYPE))
                )
                .join(memberPointEntity) //포인트 조회를 위해 join
                .on(memberPointEntity.memberId.eq(memberId))
                .where(auctionItemEntity.itemStatus.eq(ItemStatus.ACTIVE) //게시 중인 경매상품
                        .and(auctionItemEntity.transactionStatus.eq(TransactionStatus.ONGOING)) //현재 거래중 상태인 경매상품
                        .and(cursor != null ? auctionItemEntity.startTime.lt(cursor) : null))
                .orderBy(auctionItemEntity.startTime.desc())
                .limit(SIZE + 1)
                .fetch();

        for (GetAuctionItemsResponse response : auctionItemListResponses) {
            response.setParticipantCount(countParticipantById(response.getAuctionItemId()));
        }

        boolean hasNext = auctionItemListResponses.size() > SIZE; //true: 조회할 상품이 더 남은 상태 (조회 결과 : 11개)
        if (hasNext) {
            auctionItemListResponses.remove(SIZE);
        }

        LocalDateTime localStartTime = !auctionItemListResponses.isEmpty() ? auctionItemListResponses.get(auctionItemListResponses.size() - 1).getStartTime() : null;

        return new SliceResponse<>(
                new SliceImpl<>(auctionItemListResponses, PageRequest.of(0, SIZE), hasNext), localStartTime
        );
    }

    @Override
    public SliceResponse<GetMyOrMemberAuctionItemsResponse> getMyOrMemberAuctionItems(Long memberId, LocalDateTime cursor) {

        List<GetMyOrMemberAuctionItemsResponse> memberAuctionItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetMyOrMemberAuctionItemsResponse.class,
                        auctionItemEntity.id,
                        auctionItemEntity.auctionItemDetailsEntity.title,
                        auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice,
                        Expressions.constant(TransactionMode.AUCTION),
                        auctionItemEntity.transactionStatus,
                        itemImageEntity.imageName,
                        //TODO like 테이블 생기면 join해서 해당 값 채우기
                        Expressions.constant(0L),
                        auctionItemEntity.startTime,
                        auctionItemEntity.itemStatus)
                )
                .from(auctionItemEntity)
                .join(itemImageEntity) //썸네일 조회를 위해 join
                .on(itemImageEntity.itemId.eq(auctionItemEntity.id)
                        .and(itemImageEntity.isThumbnail.isTrue())
                        .and(itemImageEntity.itemType.eq(ITEM_TYPE))
                )
                .join(memberPointEntity) //포인트 조회를 위해 join
                .on(memberPointEntity.memberId.eq(memberId))
                .where(auctionItemEntity.itemStatus.eq(ItemStatus.ACTIVE) //게시 중인 경매상품
                        .and(auctionItemEntity.transactionStatus.eq(TransactionStatus.ONGOING)) //현재 거래중 상태인 경매상품
                        .and(auctionItemEntity.memberId.eq(memberId)) //본인이 게시한 거 조회
                        .and(cursor != null ? auctionItemEntity.startTime.lt(cursor) : null))
                .orderBy(auctionItemEntity.startTime.desc())
                .limit(SIZE + 1)
                .fetch();

        boolean hasNext = memberAuctionItemListResponses.size() > SIZE; //true: 조회할 상품이 더 남은 상태 (조회 결과 : 11개)
        if (hasNext) {
            memberAuctionItemListResponses.remove(SIZE);
        }

        LocalDateTime localCreateTime = !memberAuctionItemListResponses.isEmpty() ? memberAuctionItemListResponses.get(memberAuctionItemListResponses.size() - 1).getCreateAt() : null;

        return new SliceResponse<>(
                new SliceImpl<>(memberAuctionItemListResponses, PageRequest.of(0, SIZE), hasNext), localCreateTime
        );
    }

    @Override
    public GetAuctionItemResponse findAuctionItemByMemberIdAndId(Long memberId, Long id) {
        return Optional.ofNullable(jpaQueryFactory
                        .select(Projections.constructor(GetAuctionItemResponse.class,
                                Projections.constructor(AuctionItemDetailsResponse.class,
                                        auctionItemEntity.auctionItemDetailsEntity.title,
                                        auctionItemEntity.auctionItemDetailsEntity.category,
                                        auctionItemEntity.auctionItemDetailsEntity.content),
                                Projections.constructor(AuctionItemStatsResponse.class,
                                        auctionItemEntity.viewCount,
                                        //TODO likeCount로 이후에 변경하기 (좋아요 기능 만들면)
                                        auctionItemEntity.viewCount,
                                        Expressions.constant(countParticipantById(id))),
                                Expressions.constant(new ArrayList<>()), //이후 setter를 이용해 값 설정
                                auctionItemEntity.transactionType,
                                auctionItemEntity.startTime,
                                auctionItemEntity.endTime,
                                auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice,
                                auctionItemEntity.memberId, //상품 주인 id
                                memberEntity.memberPrivateInformationEntity.nickname, //상품 주인 닉네임
                                auctionItemEntity.memberId.eq(memberId), //상품 주인과 조회한 회원의 일치 여부
                                auctionItemEntity.itemStatus))
                        .from(auctionItemEntity)
                        .join(memberEntity) //마켓 주인 닉네임 조회를 위해
                        .on(memberEntity.id.eq(auctionItemEntity.memberId))
                        .where(auctionItemEntity.id.eq(id)
                                .and(auctionItemEntity.itemStatus.eq(ItemStatus.ACTIVE)))
                        .fetchOne())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.AUCTIONITEM_NOT_FOUND)
                );
    }

    @Override
    public void updateCurrentPrice(Long id, int point) {
        jpaQueryFactory.update(auctionItemEntity)
                .set(auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice, auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice.add(point))
                .where(auctionItemEntity.id.eq(id))
                .execute();
    }

    @Override
    public boolean checkMemberPointById(Long id, Long memberId, int point) {
        // 개별조회된 상태에서 경매를 진행하기에 경매상품 존재유무 체크 안함
        Integer fetchOne = jpaQueryFactory.selectOne()
                .from(auctionItemEntity)
                .join(memberPointEntity)
                .on(memberPointEntity.memberId.eq(memberId))
                .where(auctionItemEntity.id.eq(id)
                        .and(memberPointEntity.point.goe(auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice.add(point))) // 입찰 후 포인트를 회원이 갖고 있어야 함
                        .and(auctionItemEntity.auctionItemPriceDetailsEntity.bidPrice.loe(point))) // 호가단위 <= 입찰포인트
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public boolean canMemberBidByIdAndMemberId(Long id, Long memberId) {
        Integer fetchOne = jpaQueryFactory.selectOne()
                .from(auctionItemEntity)
                .where(auctionItemEntity.id.eq(id)
                        .and(auctionItemEntity.memberId.ne(memberId)))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public int getCurrentPriceById(Long auctionId) {
        return jpaQueryFactory.selectFrom(auctionItemEntity)
                .where(auctionItemEntity.id.eq(auctionId))
                .fetchOne().getAuctionItemPriceDetailsEntity().getCurrentPrice();
    }

    @Override
    public AuctionItem getById(Long id) {
        return auctionItemJpaRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.AUCTIONITEM_NOT_FOUND)).toModel();
    }

    @Override
    public SliceResponse<GetAuctionItemsResponse> searchAuctionItems(String q, Long memberId, LocalDateTime cursor) {

        List<GetAuctionItemsResponse> auctionItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetAuctionItemsResponse.class,
                        auctionItemEntity.id,
                        auctionItemEntity.auctionItemDetailsEntity.title,
                        auctionItemEntity.auctionItemDetailsEntity.category,
                        Projections.constructor(AuctionItemPriceDetailResponse.class,
                                auctionItemEntity.auctionItemPriceDetailsEntity.startPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.bidPrice),
                        Expressions.constant(0L),
                        itemImageEntity.imageName,
                        auctionItemEntity.startTime,
                        auctionItemEntity.endTime,
                        memberPointEntity.point))
                .from(auctionItemEntity)
                .join(itemImageEntity) //썸네일 조회를 위해 join
                .on(itemImageEntity.itemId.eq(auctionItemEntity.id)
                        .and(itemImageEntity.isThumbnail.isTrue())
                        .and(itemImageEntity.itemType.eq(ITEM_TYPE))
                )
                .join(memberPointEntity) //포인트 조회를 위해 join
                .on(memberPointEntity.memberId.eq(memberId))
                .where(auctionItemEntity.itemStatus.eq(ItemStatus.ACTIVE) //게시 중인 경매상품
                        .and(auctionItemEntity.transactionStatus.eq(TransactionStatus.ONGOING)) //현재 거래중 상태인 경매상품
                        .and(cursor != null ? auctionItemEntity.startTime.lt(cursor) : null)
                        .and(auctionItemEntity.auctionItemDetailsEntity.title.containsIgnoreCase(q))
                        .or(auctionItemEntity.auctionItemDetailsEntity.content.containsIgnoreCase(q)))
                .orderBy(auctionItemEntity.startTime.desc())
                .limit(SIZE + 1)
                .fetch();

        for (GetAuctionItemsResponse response : auctionItemListResponses) {
            response.setParticipantCount(countParticipantById(response.getAuctionItemId()));
        }

        boolean hasNext = auctionItemListResponses.size() > SIZE; //true: 조회할 상품이 더 남은 상태 (조회 결과 : 11개)
        if (hasNext) {
            auctionItemListResponses.remove(SIZE);
        }

        LocalDateTime localStartTime = !auctionItemListResponses.isEmpty() ? auctionItemListResponses.get(auctionItemListResponses.size() - 1).getStartTime() : null;

        return new SliceResponse<>(
                new SliceImpl<>(auctionItemListResponses, PageRequest.of(0, SIZE), hasNext), localStartTime
        );
    }

    @Override
    public Long countParticipantById(Long id) {
        Long auctionRoomId = auctionRoomRepository.findByAuctionItemId(id).orElse(0L);

        if (auctionRoomId.equals(0L)) return 0L;

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("auctionRoomId").is(auctionRoomId)),
                Aggregation.group().addToSet("bidderId").as("bidderIds"), //중복 참여자 제외
                Aggregation.project()
                        .and("bidderIds").size().as("participantCount")
        );

        AggregationResults<AuctionParticipateResponse> auctionParticipateResponse = mongoTemplate.aggregate(
                aggregation, "auction_bid", AuctionParticipateResponse.class
        );

        return Optional.ofNullable(auctionParticipateResponse.getUniqueMappedResult())
                .map(AuctionParticipateResponse::getParticipantCount)
                .orElse(0L);
    }

    @Override
    public Long countAuctionItemByMemberId(Long memberId) {
        return jpaQueryFactory.select(auctionItemEntity.count())
                .from(auctionItemEntity)
                .where(auctionItemEntity.memberId.eq(memberId)
                        .and(auctionItemEntity.itemStatus.eq(ItemStatus.ACTIVE)))
                .fetchOne();
    }
}
