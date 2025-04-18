package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.AuctionItemPriceDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.GetAuctionItemListResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemStatsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.AuctionItemEntity;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
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
import static com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.QAuctionItemEntity.auctionItemEntity;
import static com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity.memberEntity;
import static com.yzgeneration.evc.domain.point.infrastructure.QMemberPointEntity.memberPointEntity;

@Repository
@RequiredArgsConstructor
public class AuctionItemRepositoryImpl implements AuctionItemRepository {
    private final AuctionItemJpaRepository auctionItemJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private static final int SIZE = 10;
    private static final ItemType ITEM_TYPE = ItemType.AUCTIONITEM;

    @Override
    public AuctionItem save(AuctionItem auctionItem) {
        return auctionItemJpaRepository.save(AuctionItemEntity.from(auctionItem)).toModel();
    }

    @Override
    public SliceResponse<GetAuctionItemListResponse> getAuctionItemList(Long memberId, LocalDateTime cursor) {

        List<GetAuctionItemListResponse> auctionItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetAuctionItemListResponse.class,
                        auctionItemEntity.id,
                        auctionItemEntity.auctionItemDetailsEntity.title,
                        Projections.constructor(AuctionItemPriceDetailsResponse.class,
                                auctionItemEntity.auctionItemPriceDetailsEntity.startPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.bidPrice),
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
    public GetAuctionItemResponse findByIdAndMemberId(Long memberId, Long id) {
        return Optional.ofNullable(jpaQueryFactory
                        .select(Projections.constructor(GetAuctionItemResponse.class,
                                Projections.constructor(AuctionItemDetailsResponse.class,
                                        auctionItemEntity.auctionItemDetailsEntity.title,
                                        auctionItemEntity.auctionItemDetailsEntity.category,
                                        auctionItemEntity.auctionItemDetailsEntity.content),
                                Projections.constructor(AuctionItemStatsResponse.class,
                                        auctionItemEntity.auctionItemStatsEntity.viewCount,
                                        //TODO likeCount로 이후에 변경하기 (좋아요 기능 만들면)
                                        auctionItemEntity.auctionItemStatsEntity.viewCount,
                                        auctionItemEntity.auctionItemStatsEntity.participantCount),
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
                        .where(auctionItemEntity.id.eq(id))
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
    public SliceResponse<GetAuctionItemListResponse> searchAuctionItemList(String q, Long memberId, LocalDateTime cursor) {

        List<GetAuctionItemListResponse> auctionItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetAuctionItemListResponse.class,
                        auctionItemEntity.id,
                        auctionItemEntity.auctionItemDetailsEntity.title,
                        Projections.constructor(AuctionItemPriceDetailsResponse.class,
                                auctionItemEntity.auctionItemPriceDetailsEntity.startPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.bidPrice),
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

        boolean hasNext = auctionItemListResponses.size() > SIZE; //true: 조회할 상품이 더 남은 상태 (조회 결과 : 11개)
        if (hasNext) {
            auctionItemListResponses.remove(SIZE);
        }

        LocalDateTime localStartTime = !auctionItemListResponses.isEmpty() ? auctionItemListResponses.get(auctionItemListResponses.size() - 1).getStartTime() : null;

        return new SliceResponse<>(
                new SliceImpl<>(auctionItemListResponses, PageRequest.of(0, SIZE), hasNext), localStartTime
        );
    }
}
