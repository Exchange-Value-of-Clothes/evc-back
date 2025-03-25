package com.yzgeneration.evc.domain.member.infrastructure;

import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.domain.verification.model.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.yzgeneration.evc.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class EmailSender implements MailSender {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Async
    @Override
    public void send(Email email) { // TODO 쓰레드 풀 설정
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
            throw new CustomException(SMTP_SERVER_ERROR);
        }
    }
}
