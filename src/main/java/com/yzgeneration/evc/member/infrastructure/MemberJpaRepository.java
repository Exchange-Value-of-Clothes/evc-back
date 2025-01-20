package com.yzgeneration.evc.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByMemberPrivateInformationEntityEmail(String memberPrivateInformationEntityEmail);
}
