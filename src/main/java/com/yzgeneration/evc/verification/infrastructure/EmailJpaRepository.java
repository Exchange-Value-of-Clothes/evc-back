package com.yzgeneration.evc.verification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailJpaRepository extends JpaRepository<EmailVerificationEntity, Long> {
}
