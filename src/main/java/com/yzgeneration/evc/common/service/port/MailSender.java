package com.yzgeneration.evc.common.service.port;

import com.yzgeneration.evc.verification.model.Email;

public interface MailSender {

    void send(Email email);
}
