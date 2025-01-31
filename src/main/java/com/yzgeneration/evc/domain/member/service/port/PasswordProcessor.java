package com.yzgeneration.evc.domain.member.service.port;

public interface PasswordProcessor {
    String encode(String rawPassword);
    Boolean matches(String rawPassword, String encodedPassword);
}
