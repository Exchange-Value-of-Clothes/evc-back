package com.yzgeneration.evc.mock.like;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.like.model.Like;
import com.yzgeneration.evc.domain.like.service.port.LikeRepository;

public class FakeLikeRepository implements LikeRepository {
    @Override
    public Like save(Like like) {
        return null;
    }

    @Override
    public Long countByItemIdAndItemType(Long itemId, ItemType itemType) {
        return null;
    }

    @Override
    public Like getLike(Long memberId, Long itemId, ItemType itemType) {
        return null;
    }

    @Override
    public boolean existsByMemberIdAndItemIdAndItemType(Long memberId, Long itemId, ItemType itemType) {
        return false;
    }

    @Override
    public void deleteByMemberIdAndItemIdAndItemType(Long memberId, Long itemId, ItemType itemType) {

    }
}
