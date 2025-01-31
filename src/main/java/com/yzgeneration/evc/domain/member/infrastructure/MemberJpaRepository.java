package com.yzgeneration.evc.domain.member.infrastructure;

import com.yzgeneration.evc.domain.member.enums.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByMemberPrivateInformationEntityEmail(String email);
    Optional<MemberEntity> findByMemberPrivateInformationEntityEmail(String email);
    Optional<MemberEntity> findByMemberAuthenticationInformationEntityProviderTypeAndMemberAuthenticationInformationEntityProviderId(ProviderType providerType, String providerId);
}
