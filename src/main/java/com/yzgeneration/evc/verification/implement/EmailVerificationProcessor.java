package com.yzgeneration.evc.verification.implement;

import com.yzgeneration.evc.common.service.port.UuidHolder;
import com.yzgeneration.evc.verification.enums.EmailVerificationType;
import com.yzgeneration.evc.verification.infrastructure.EmailVerificationRepository;
import com.yzgeneration.evc.verification.model.Email;
import com.yzgeneration.evc.verification.model.EmailVerification;
import com.yzgeneration.evc.member.service.port.MailSender;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class EmailVerificationProcessor {

    private final EmailVerificationRepository emailVerificationRepository;
    private final UuidHolder uuidHolder;
    private final MailSender mailSender;

    public EmailVerification createEmailVerification(Long userId, String emailAddress, EmailVerificationType emailVerificationType) {
        return emailVerificationRepository.save(EmailVerification.create(userId, emailAddress, uuidHolder, emailVerificationType));
    }
    
    public void sendMail(EmailVerification emailVerification) {
        mailSender.send(Email.create(emailVerification));
    }


}
