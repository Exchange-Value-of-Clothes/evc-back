package com.yzgeneration.evc.domain.member.infrastructure;

public interface PasswordProcessor {
    String encode(String rawPassword);
    Boolean matches(String rawPassword, String encodedPassword);
}
