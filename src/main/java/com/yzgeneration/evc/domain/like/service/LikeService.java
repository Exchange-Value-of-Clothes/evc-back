package com.yzgeneration.evc.domain.like.service;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.like.dto.LikeResponse;
import com.yzgeneration.evc.domain.like.implement.LikeChecker;
import com.yzgeneration.evc.domain.like.model.Like;
import com.yzgeneration.evc.domain.like.service.port.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeChecker likeChecker;

    public LikeResponse toggleLike(Long memberId, Long itemId, ItemType itemType) {
        likeChecker.isSelfLike(memberId, itemId, itemType); //본인 물품에는 좋아요 안됨

        boolean exits = likeRepository.existsByMemberIdAndItemIdAndItemType(memberId, itemId, itemType);
        if (exits) {
            likeRepository.deleteByMemberIdAndItemIdAndItemType(memberId, itemId, itemType);

        } else {
            likeRepository.save(Like.create(memberId, itemId, itemType));
        }

        return new LikeResponse(!exits, likeRepository.countByItemIdAndItemType(itemId, itemType));
    }
}
