package com.yzgeneration.evc.verification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailJpaRepository extends JpaRepository<EmailVerificationEntity, Long> {
    Optional<EmailVerificationEntity> findByVerificationCode(String verificationCode);
}
