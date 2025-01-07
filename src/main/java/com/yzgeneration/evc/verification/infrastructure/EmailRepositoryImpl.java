package com.yzgeneration.evc.verification.infrastructure;

import com.yzgeneration.evc.verification.model.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailRepositoryImpl implements EmailRepository{

    private final EmailJpaRepository emailJpaRepository;

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailJpaRepository.save(EmailVerificationEntity.from(emailVerification)).toModel();
    }
}
