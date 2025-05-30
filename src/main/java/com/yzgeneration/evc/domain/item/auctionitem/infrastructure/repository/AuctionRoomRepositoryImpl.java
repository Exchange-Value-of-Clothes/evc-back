package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.AuctionRoomEntity;
import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionRoom;
import com.yzgeneration.evc.domain.item.auctionitem.service.port.AuctionRoomRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.QAuctionRoomEntity.auctionRoomEntity;

@Repository
@RequiredArgsConstructor
public class AuctionRoomRepositoryImpl implements AuctionRoomRepository {
    private final AuctionRoomJpaRepository auctionRoomJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long save(AuctionRoom auctionRoom) {
        return auctionRoomJpaRepository.save(AuctionRoomEntity.from(auctionRoom)).toModel().getId();
    }

    @Override
    public Optional<Long> findByAuctionItemId(Long auctionItemId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(auctionRoomEntity)
                .where(auctionRoomEntity.auctionItemId.eq(auctionItemId))
                .fetchFirst()).map(AuctionRoomEntity::getId);
    }

    @Override
    public Long findAuctionItemIdById(Long id) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(auctionRoomEntity)
                        .where(auctionRoomEntity.id.eq(id))
                        .fetchFirst())
                .map(AuctionRoomEntity::getAuctionItemId)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.AUCTIONITEM_NOT_FOUND)
                );
    }
}
