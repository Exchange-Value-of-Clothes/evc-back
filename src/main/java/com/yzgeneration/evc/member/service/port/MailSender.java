package com.yzgeneration.evc.member.service.port;

import com.yzgeneration.evc.verification.model.Email;

public interface MailSender {

    void send(Email email);
}
