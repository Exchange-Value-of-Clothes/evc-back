package com.yzgeneration.evc.verification.model;


import com.yzgeneration.evc.common.service.port.UuidHolder;
import com.yzgeneration.evc.verification.enums.EmailVerificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVerification {

    private Long id;
    private Long memberId;
    private String emailAddress;
    private String verificationCode;
    private EmailVerificationType emailVerificationType;
    private boolean isVerified;

    public static EmailVerification create(Long memberId, String emailAddress, UuidHolder uuidHolder,
                                           EmailVerificationType emailVerificationType) {
        return EmailVerification.builder()
                .memberId(memberId)
                .emailAddress(emailAddress)
                .verificationCode(uuidHolder.random())
                .emailVerificationType(emailVerificationType)
                .build();
    }

    public void verify() {
        this.isVerified = true;
    }

}
