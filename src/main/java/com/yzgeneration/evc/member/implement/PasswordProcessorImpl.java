package com.yzgeneration.evc.member.implement;

import com.yzgeneration.evc.member.service.port.PasswordProcessor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordProcessorImpl implements PasswordProcessor {

    public PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public Boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
