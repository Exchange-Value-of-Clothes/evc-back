package com.yzgeneration.evc.verification.infrastructure;

import com.yzgeneration.evc.verification.model.EmailVerification;

public interface EmailRepository {
    EmailVerification save(EmailVerification emailVerification);
}
