package com.yzgeneration.evc.domain.like.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.like.infrastructure.entity.LikeEntity;
import com.yzgeneration.evc.domain.like.model.Like;
import com.yzgeneration.evc.domain.like.service.port.LikeRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.yzgeneration.evc.domain.like.infrastructure.entity.QLikeEntity.likeEntity;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
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

    @Override
    public SliceResponse<Like> getLikesByMemberIdAndSize(Long memberId, int size) {
        List<Like> likes = jpaQueryFactory.selectFrom(likeEntity)
                .where(likeEntity.memberId.eq(memberId))
                .orderBy(likeEntity.createAt.desc()) //최신순
                .limit(size + 1)
                .fetch()
                .stream()
                .map(LikeEntity::toModel)
                .collect(Collectors.toList());

        boolean hasNext = likes.size() > size; //true: 조회할 상품이 더 남은 상태 (조회 결과 : 11개)
        if (hasNext) {
            likes.remove(size);
        }

        LocalDateTime localCreateTime = !likes.isEmpty() ? likes.get(likes.size() - 1).getCreateAt() : null;

        return new SliceResponse<>(
                new SliceImpl<>(likes, PageRequest.of(0, size), hasNext), localCreateTime
        );
    }
}
