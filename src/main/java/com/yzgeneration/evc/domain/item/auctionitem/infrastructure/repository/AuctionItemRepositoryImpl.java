package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.AuctionItemPriceDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.GetAuctionItemListResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemStatsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.AuctionItemEntity;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionItem;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionItemRepository;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.list;
import static com.yzgeneration.evc.domain.image.infrastructure.entity.QImageEntity.imageEntity;
import static com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.QAuctionItemEntity.auctionItemEntity;
import static com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity.memberEntity;

@Repository
@RequiredArgsConstructor
public class AuctionItemRepositoryImpl implements AuctionItemRepository {
    private final AuctionItemJpaRepository auctionItemJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public AuctionItem save(AuctionItem auctionItem) {
        return auctionItemJpaRepository.save(AuctionItemEntity.from(auctionItem)).toModel();
    }

    @Override
    public SliceResponse<GetAuctionItemListResponse> getAuctionItemList(Long memberId, LocalDateTime cursor) {

        int size = 10;

        List<GetAuctionItemListResponse> auctionItemListResponses = jpaQueryFactory
                .select(Projections.constructor(GetAuctionItemListResponse.class,
                        auctionItemEntity.id,
                        auctionItemEntity.auctionItemDetailsEntity.title,
                        Projections.constructor(AuctionItemPriceDetailsResponse.class,
                                auctionItemEntity.auctionItemPriceDetailsEntity.startPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice,
                                auctionItemEntity.auctionItemPriceDetailsEntity.bidPrice),
                        imageEntity.imageName,
                        auctionItemEntity.startTime,
                        auctionItemEntity.endTime,
                        memberEntity.memberPrivateInformationEntity.point))
                .from(auctionItemEntity)
                .join(imageEntity) //썸네일 조회를 위해 join
                .on(imageEntity.itemId.eq(auctionItemEntity.id))
                .join(memberEntity) //포인트 조회를 위해 join
                .on(memberEntity.id.eq(memberId))
                .where(imageEntity.isThumbnail
                        .and(auctionItemEntity.itemStatus.eq(ItemStatus.ACTIVE)) //게시 중인 경매상품
                        .and(auctionItemEntity.transactionStatus.eq(TransactionStatus.ONGOING)) //현재 거래중 상태인 경매상품
                        .and(cursor != null ? auctionItemEntity.startTime.lt(cursor) : null))
                .orderBy(auctionItemEntity.startTime.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = auctionItemListResponses.size() > size; //true: 조회할 상품이 더 남은 상태 (조회 결과 : 11개)
        if (hasNext) {
            auctionItemListResponses.remove(size);
        }

        LocalDateTime localStartTime = !auctionItemListResponses.isEmpty() ? auctionItemListResponses.get(auctionItemListResponses.size() - 1).getStartTime() : null;

        return new SliceResponse<>(
                new SliceImpl<>(auctionItemListResponses, PageRequest.of(0, size), hasNext), localStartTime
        );
    }

    @Override
    public Optional<GetAuctionItemResponse> findByAuctionItemByItemId(Long memberId, Long itemId) {
        GetAuctionItemResponse auctionItemResponse = jpaQueryFactory
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
                        list(), //이후 setter를 이용해 값 설정
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
                .where(auctionItemEntity.id.eq(itemId))
                .fetchOne();

        return Optional.ofNullable(auctionItemResponse);
    }
}
