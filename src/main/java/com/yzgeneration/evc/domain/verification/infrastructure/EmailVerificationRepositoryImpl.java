package com.yzgeneration.evc.domain.verification.infrastructure;

import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.domain.verification.model.EmailVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.yzgeneration.evc.exception.ErrorCode.*;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {

    private final EmailVerificationJpaRepository emailVerificationJpaRepository;

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailVerificationJpaRepository.save(EmailVerificationEntity.from(emailVerification)).toModel();
    }

    @Override
    public EmailVerification getByToken(String code) {
        return emailVerificationJpaRepository.findByVerificationCode(code).orElseThrow(()-> new CustomException(EMAIL_VERIFICATION_NOT_FOUND)).toModel();
    }

    @Override
    public EmailVerification getByEmail(String email) {
        return emailVerificationJpaRepository.findByEmailAddress(email).orElseThrow(()-> new CustomException(EMAIL_VERIFICATION_NOT_FOUND)).toModel();
    }
}
