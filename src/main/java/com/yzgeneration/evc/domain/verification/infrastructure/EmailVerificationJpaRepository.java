package com.yzgeneration.evc.domain.verification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationEntity, Long> {
    Optional<EmailVerificationEntity> findByVerificationCode(String verificationCode);
    Optional<EmailVerificationEntity> findByEmailAddress(String emailAddress);

}
