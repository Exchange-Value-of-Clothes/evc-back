package com.yzgeneration.evc.domain.verification.implement;

import com.yzgeneration.evc.common.implement.port.UuidHolder;
import com.yzgeneration.evc.domain.verification.enums.EmailVerificationType;
import com.yzgeneration.evc.domain.verification.infrastructure.EmailVerificationRepository;
import com.yzgeneration.evc.domain.verification.model.Email;
import com.yzgeneration.evc.domain.verification.model.EmailVerification;
import com.yzgeneration.evc.domain.member.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationProcessor {

    private final EmailVerificationRepository emailVerificationRepository;
    private final UuidHolder uuidHolder;
    private final MailSender mailSender;

    public EmailVerification createEmailVerification(Long memberId, String emailAddress, EmailVerificationType emailVerificationType) {
        return emailVerificationRepository.save(EmailVerification.create(memberId, emailAddress, uuidHolder, emailVerificationType));
    }
    
    public void sendMail(EmailVerification emailVerification) {
        mailSender.send(Email.create(emailVerification));
    }

    public Long verify(String code) {
        EmailVerification emailVerification = emailVerificationRepository.getByToken(code);
        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);
        return emailVerification.getMemberId();
    }

    public String get(String email) {
        return emailVerificationRepository.getByEmail(email).getVerificationCode();
    }
}
