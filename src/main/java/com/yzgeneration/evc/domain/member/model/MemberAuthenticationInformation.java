package com.yzgeneration.evc.domain.member.model;

import com.yzgeneration.evc.domain.member.enums.ProviderType;
import com.yzgeneration.evc.domain.member.infrastructure.PasswordProcessor;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
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

    public void changePassword(String oldPassword, String newPassword, PasswordProcessor passwordProcessor) {
        if (providerType != ProviderType.EMAIL) throw new CustomException(ErrorCode.INVALID_PROVIDER_TYPE);
        if (!passwordProcessor.matches(oldPassword, password)) throw new CustomException(ErrorCode.INVALID_PASSWORD);
        this.password = passwordProcessor.encode(newPassword);
    }

}
