package com.yzgeneration.evc.mock.member;

import com.yzgeneration.evc.member.service.port.MailSender;
import com.yzgeneration.evc.verification.model.Email;

public class DummyEmailSender implements MailSender {

    @Override
    public void send(Email email) {

    }
}
