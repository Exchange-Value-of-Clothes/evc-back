package com.yzgeneration.evc.authentication.service;

import com.yzgeneration.evc.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.authentication.implement.AuthenticationProcessor;
import com.yzgeneration.evc.authentication.implement.TokenProvider;
import com.yzgeneration.evc.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationProcessor authenticationProcessor;
    private final TokenProvider tokenProvider;

    public AuthenticationToken login(String email, String password) {
        Member member = authenticationProcessor.login(email, password);
        return tokenProvider.create(member.getId());
    }
}
