package com.yzgeneration.evc.domain.member.infrastructure;

import com.yzgeneration.evc.domain.member.enums.ProviderType;
import com.yzgeneration.evc.domain.member.model.MemberAuthenticationInformation;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthenticationInformationEntity {

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;
    private String providerId;
    private String password;

    public static MemberAuthenticationInformationEntity from(MemberAuthenticationInformation memberAuthenticationInformation) {
        return MemberAuthenticationInformationEntity.builder()
                .providerType(memberAuthenticationInformation.getProviderType())
                .providerId(memberAuthenticationInformation.getProviderId())
                .password(memberAuthenticationInformation.getPassword())
                .build();
    }

    public MemberAuthenticationInformation toModel() {
        return MemberAuthenticationInformation.builder()
                .providerType(providerType)
                .providerId(providerId)
                .password(password)
                .build();
    }
}
