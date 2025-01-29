package com.yzgeneration.evc.domain.verification.infrastructure;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.domain.verification.model.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.yzgeneration.evc.common.exception.ErrorCode.*;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {

    private final EmailJpaRepository emailJpaRepository;

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailJpaRepository.save(EmailVerificationEntity.from(emailVerification)).toModel();
    }

    @Override
    public EmailVerification getByToken(String code) {
        return emailJpaRepository.findByVerificationCode(code).orElseThrow(()-> new CustomException(EMAIL_VERIFICATION_NOT_FOUND)).toModel();
    }

    @Override
    public EmailVerification getByEmail(String email) {
        return emailJpaRepository.findByEmailAddress(email).orElseThrow(()-> new CustomException(EMAIL_VERIFICATION_NOT_FOUND)).toModel();
    }
}
