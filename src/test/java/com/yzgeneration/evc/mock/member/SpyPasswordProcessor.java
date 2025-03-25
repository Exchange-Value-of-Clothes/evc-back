package com.yzgeneration.evc.mock.member;

import com.yzgeneration.evc.domain.member.infrastructure.PasswordProcessor;

public class SpyPasswordProcessor implements PasswordProcessor {
    @Override
    public String encode(String rawPassword) {
        return rawPassword;
    }

    @Override
    public Boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
