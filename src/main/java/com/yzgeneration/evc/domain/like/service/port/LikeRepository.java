package com.yzgeneration.evc.domain.like.service.port;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.like.model.Like;

public interface LikeRepository {
    Like save(Like like);

    Long countByItemIdAndItemType(Long itemId, ItemType itemType);

    boolean existsByMemberIdAndItemIdAndItemType(Long memberId, Long itemId, ItemType itemType);

    void deleteByMemberIdAndItemIdAndItemType(Long memberId, Long itemId, ItemType itemType);

    SliceResponse<Like> getLikesByMemberIdAndSize(Long memberId, int size);
}
