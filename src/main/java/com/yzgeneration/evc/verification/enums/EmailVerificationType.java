package com.yzgeneration.evc.verification.enums;

import com.yzgeneration.evc.verification.template.EmailVerificationTemplate;
import com.yzgeneration.evc.verification.template.RegisterEmailVerificationTemplate;

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
