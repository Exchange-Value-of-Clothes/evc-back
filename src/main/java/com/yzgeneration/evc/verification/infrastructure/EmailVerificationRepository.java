package com.yzgeneration.evc.verification.infrastructure;

import com.yzgeneration.evc.verification.model.EmailVerification;

public interface EmailVerificationRepository {
    EmailVerification save(EmailVerification emailVerification);
}
