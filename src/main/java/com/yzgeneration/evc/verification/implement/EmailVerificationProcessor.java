package com.yzgeneration.evc.verification.implement;

import com.yzgeneration.evc.common.service.port.UuidHolder;
import com.yzgeneration.evc.verification.enums.EmailVerificationType;
import com.yzgeneration.evc.verification.infrastructure.EmailRepository;
import com.yzgeneration.evc.verification.model.Email;
import com.yzgeneration.evc.verification.model.EmailVerification;
import com.yzgeneration.evc.common.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationProcessor {

    private final EmailRepository emailRepository;
    private final UuidHolder uuidHolder;
    private final MailSender mailSender;

    public EmailVerification createEmailVerification(Long userId, String emailAddress, EmailVerificationType emailVerificationType) {
        return emailRepository.save(EmailVerification.create(userId, emailAddress, uuidHolder, emailVerificationType));
    }
    
    public void sendMail(EmailVerification emailVerification) {
        mailSender.send(Email.create(emailVerification));
    }


}
