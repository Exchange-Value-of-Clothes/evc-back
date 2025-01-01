package com.yzgeneration.evc.member.service.port;

public interface PasswordProcessor {
    String encode(String rawPassword);

    Boolean matches(String rawPassword, String encodedPassword);
}
