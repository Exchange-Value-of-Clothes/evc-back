package com.yzgeneration.evc.domain.verification.enums;

import com.yzgeneration.evc.domain.verification.template.EmailVerificationTemplate;
import com.yzgeneration.evc.domain.verification.template.RegisterEmailVerificationTemplate;

public enum EmailVerificationType {
    REGISTER {
        @Override
        public EmailVerificationTemplate createTemplate(String verificationCode) {
            return new RegisterEmailVerificationTemplate(verificationCode);
        }
    },
    CHANGE_PASSWORD {
        @Override
        public EmailVerificationTemplate createTemplate(String verificationCode) {
            return null;
        }
    };

    public abstract EmailVerificationTemplate createTemplate(String verificationCode);
}
