package com.yzgeneration.evc.domain.item.useditem.infrastructure.repository;

import com.yzgeneration.evc.domain.item.useditem.infrastructure.entity.UsedItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsedItemJpaRepository extends JpaRepository<UsedItemEntity, Long> {

    @Query("""
            SELECT m.memberPrivateInformationEntity.nickname FROM UsedItemEntity u
            LEFT JOIN MemberEntity m
            ON u.memberId = m.id
            WHERE u.id = :usedItemId
             """)
    Optional<String> findNicknameByUsedItemId(Long usedItemId);

    Page<UsedItemEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
