package com.yzgeneration.evc.domain.member.authentication.service;

import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.authentication.implement.AuthenticationProcessor;
import com.yzgeneration.evc.domain.member.authentication.implement.TokenProvider;
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

    public AuthenticationToken refresh(String refreshToken) {
        return tokenProvider.refresh(refreshToken);
    }

    public String authorizationCode(String providerType, String state) {
        return authenticationProcessor.getAuthorizationCode(providerType, state);
    }
}
