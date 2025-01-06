package com.yzgeneration.evc.common.service.infrastructure;

import com.yzgeneration.evc.common.service.port.MailSender;
import com.yzgeneration.evc.verification.model.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender implements MailSender {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void send(Email email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email.getTo());
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setText(email.getContent(), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
