package com.yzgeneration.evc.domain.member.model;

import com.yzgeneration.evc.domain.member.enums.ProviderType;
import com.yzgeneration.evc.domain.member.service.port.PasswordProcessor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberAuthenticationInformation {
    private ProviderType providerType;
    private String providerId;
    private String password;

    public static MemberAuthenticationInformation createdByEmail(String rawPassword, PasswordProcessor passwordProcessor) {
        return MemberAuthenticationInformation.builder()
                .providerType(ProviderType.EMAIL)
                .password(passwordProcessor.encode(rawPassword))
                .build();
    }

    public static MemberAuthenticationInformation createdBySocialLogin(String providerType, String providerId) {
        return MemberAuthenticationInformation.builder()
                .providerType(ProviderType.valueOf(providerType))
                .providerId(providerId)
                .build();
    }

}
