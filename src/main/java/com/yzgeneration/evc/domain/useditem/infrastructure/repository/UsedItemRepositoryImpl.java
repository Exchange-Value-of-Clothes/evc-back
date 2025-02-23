package com.yzgeneration.evc.domain.useditem.infrastructure.repository;


import com.yzgeneration.evc.domain.useditem.infrastructure.entity.UsedItemEntity;
import com.yzgeneration.evc.domain.useditem.model.UsedItem;
import com.yzgeneration.evc.domain.useditem.service.port.UsedItemRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsedItemRepositoryImpl implements UsedItemRepository {
    private final UsedItemJpaRepository usedItemJpaRepository;

    @Override
    public UsedItem save(UsedItem usedItem) {
        return usedItemJpaRepository.save(UsedItemEntity.from(usedItem)).toModel();
    }

    @Override
    public Slice<UsedItem> findAll(Pageable pageable) {
        return usedItemJpaRepository.findAllByOrderByCreatedAtDesc(pageable).map(UsedItemEntity::toModel);
    }

    @Override
    public UsedItem findById(Long usedItemId) {
        return usedItemJpaRepository.findById(usedItemId).orElseThrow(
                () -> new CustomException(ErrorCode.USEDITEM_NOT_FOUND)
        ).toModel();
    }

    @Override
    public String findNicknameByUsedItemId(Long usedItemId) {
        return usedItemJpaRepository.findNicknameByUsedItemId(usedItemId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }
}
