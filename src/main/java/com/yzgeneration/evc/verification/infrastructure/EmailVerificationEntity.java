package com.yzgeneration.evc.verification.infrastructure;

import com.yzgeneration.evc.verification.enums.EmailVerificationType;
import com.yzgeneration.evc.verification.model.EmailVerification;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "email_verifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String emailAddress;

    private String verificationCode;

    @Enumerated(EnumType.STRING)
    private EmailVerificationType emailVerificationType;

    private boolean isVerified;

    public static EmailVerificationEntity from(EmailVerification emailVerification) {
        return EmailVerificationEntity.builder()
                .userId(emailVerification.getUserId())
                .emailAddress(emailVerification.getEmailAddress())
                .verificationCode(emailVerification.getVerificationCode())
                .emailVerificationType(emailVerification.getEmailVerificationType())
                .isVerified(emailVerification.isVerified())
                .build();
    }

    public EmailVerification toModel() {
        return EmailVerification.builder()
                .id(id)
                .userId(userId)
                .emailAddress(emailAddress)
                .verificationCode(verificationCode)
                .emailVerificationType(emailVerificationType)
                .isVerified(isVerified)
                .build();
    }
}
