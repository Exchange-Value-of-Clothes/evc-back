package com.yzgeneration.evc.domain.like.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.like.infrastructure.entity.LikeEntity;
import com.yzgeneration.evc.domain.like.model.Like;
import com.yzgeneration.evc.domain.like.service.port.LikeRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.yzgeneration.evc.domain.like.infrastructure.entity.QLikeEntity.likeEntity;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryIml implements LikeRepository {
    private final LikeJpaRepositoy likeJpaRepositoy;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Like save(Like like) {
        return likeJpaRepositoy.save(LikeEntity.from(like)).toModel();
    }

    @Override
    public Long countByItemIdAndItemType(Long itemId, ItemType itemType) {
        return jpaQueryFactory.select(likeEntity.count())
                .from(likeEntity)
                .where(likeEntity.itemId.eq(itemId)
                        .and(likeEntity.itemType.eq(itemType)))
                .fetchOne();
    }

    @Override
    public Like getLike(Long memberId, Long itemId, ItemType itemType) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(likeEntity)
                        .where(likeEntity.memberId.eq(memberId)
                                .and(likeEntity.itemId.eq(itemId))
                                .and(likeEntity.itemType.eq(itemType)))
                        .fetchOne())
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND))
                .toModel();
    }

    @Override
    public boolean existsByMemberIdAndItemIdAndItemType(Long memberId, Long itemId, ItemType itemType) {
        Integer fetchOne = jpaQueryFactory.selectOne()
                .from(likeEntity)
                .where(likeEntity.memberId.eq(memberId)
                        .and(likeEntity.itemId.eq(itemId))
                        .and(likeEntity.itemType.eq(itemType)))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    @Transactional
    public void deleteByMemberIdAndItemIdAndItemType(Long memberId, Long itemId, ItemType itemType) {
        long row = jpaQueryFactory.delete(likeEntity)
                .where(likeEntity.memberId.eq(memberId)
                        .and(likeEntity.itemId.eq(itemId))
                        .and(likeEntity.itemType.eq(itemType)))
                .execute();
        if (row == 0) throw new CustomException(ErrorCode.LIKE_NOT_FOUND);
    }
}
