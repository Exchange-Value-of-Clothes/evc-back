package com.yzgeneration.evc.member.model;

import com.yzgeneration.evc.member.enums.ProviderType;
import com.yzgeneration.evc.member.service.port.PasswordProcessor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberAuthenticationInformation {
    private ProviderType providerType;
    private String providerId;
    private String password;

    public static MemberAuthenticationInformation createByEmail(PasswordProcessor passwordProcessor, String rawPassword) {
        return MemberAuthenticationInformation.builder()
                .providerType(ProviderType.EMAIL)
                .password(passwordProcessor.encode(rawPassword))
                .build();
    }


}
