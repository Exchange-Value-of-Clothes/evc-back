package com.yzgeneration.evc.domain.delivery.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByMemberId(Long memberId);
}
