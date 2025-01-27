package com.yzgeneration.evc.domain.verification.template;

import lombok.Getter;

@Getter
public abstract class EmailVerificationTemplate {
    protected String title;
    protected String content;

    public EmailVerificationTemplate(String title) {
        this.title = title;
    }

    protected abstract void generateContent(String verificationCode);
}
