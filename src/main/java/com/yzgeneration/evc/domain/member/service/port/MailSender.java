package com.yzgeneration.evc.domain.member.service.port;

import com.yzgeneration.evc.domain.verification.model.Email;

public interface MailSender {
    void send(Email email);
}
