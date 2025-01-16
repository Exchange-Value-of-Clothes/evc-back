package com.yzgeneration.evc.mock.member;

import com.yzgeneration.evc.member.service.port.PasswordProcessor;

public class SpyPasswordProcessor implements PasswordProcessor {
    @Override
    public String encode(String rawPassword) {
        return rawPassword;
    }

    @Override
    public Boolean matches(String rawPassword, String encodedPassword) {
        return null;
    }
}
