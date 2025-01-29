package com.yzgeneration.evc.domain.verification.infrastructure;

import com.yzgeneration.evc.domain.verification.model.EmailVerification;

public interface EmailVerificationRepository {
    EmailVerification save(EmailVerification emailVerification);
    EmailVerification getByToken(String token);
    EmailVerification getByEmail(String email);
}
