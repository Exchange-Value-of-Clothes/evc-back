package com.yzgeneration.evc.mock.member;

import com.yzgeneration.evc.domain.member.service.port.MailSender;
import com.yzgeneration.evc.domain.verification.model.Email;

public class DummyEmailSender implements MailSender {

    @Override
    public void send(Email email) {

    }
}
