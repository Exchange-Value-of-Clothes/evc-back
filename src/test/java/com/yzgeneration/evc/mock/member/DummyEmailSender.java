package com.yzgeneration.evc.mock.member;

import com.yzgeneration.evc.domain.member.infrastructure.MailSender;
import com.yzgeneration.evc.domain.verification.model.Email;

public class DummyEmailSender implements MailSender {

    @Override
    public void send(Email email) {

    }
}
