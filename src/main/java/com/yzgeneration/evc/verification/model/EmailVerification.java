package com.yzgeneration.evc.verification.model;


import com.yzgeneration.evc.common.service.port.UuidHolder;
import com.yzgeneration.evc.verification.enums.EmailVerificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVerification {

    private Long id;
    private Long userId;
    private String emailAddress;
    private String verificationCode;
    private EmailVerificationType emailVerificationType;
    private boolean isVerified;

    public static EmailVerification create(Long userId, String emailAddress, UuidHolder uuidHolder,
                                           EmailVerificationType emailVerificationType) {
        return EmailVerification.builder()
                .userId(userId)
                .emailAddress(emailAddress)
                .verificationCode(uuidHolder.random())
                .emailVerificationType(emailVerificationType)
                .build();
    }

}
