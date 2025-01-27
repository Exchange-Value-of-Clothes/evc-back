package com.yzgeneration.evc.domain.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByMemberPrivateInformationEntityEmail(String memberPrivateInformationEntityEmail);
    Optional<MemberEntity> findByMemberPrivateInformationEntityEmail(String memberPrivateInformationEntityEmail);
}
