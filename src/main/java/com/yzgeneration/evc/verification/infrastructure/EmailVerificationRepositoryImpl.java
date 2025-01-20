package com.yzgeneration.evc.verification.infrastructure;

import com.yzgeneration.evc.verification.model.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {

    private final EmailJpaRepository emailJpaRepository;

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailJpaRepository.save(EmailVerificationEntity.from(emailVerification)).toModel();
    }

    @Override
    public EmailVerification get(String code) {
        return emailJpaRepository.findByVerificationCode(code).orElseThrow(()-> new RuntimeException()).toModel();
    }
}
