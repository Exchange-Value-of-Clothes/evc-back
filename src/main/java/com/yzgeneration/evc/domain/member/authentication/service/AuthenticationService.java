package com.yzgeneration.evc.domain.member.authentication.service;

import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationResponse;
import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.authentication.implement.AuthenticationProcessor;
import com.yzgeneration.evc.domain.member.authentication.implement.TokenProvider;
import com.yzgeneration.evc.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationProcessor authenticationProcessor;
    private final TokenProvider tokenProvider;

    public ResponseEntity<AuthenticationResponse.LoginResponse> login(String email, String password) {
        Member member = authenticationProcessor.login(email, password);
        AuthenticationToken authenticationToken = tokenProvider.create(member.getId());
        return authenticationProcessor.getLoginResponse(authenticationToken);
    }

    public ResponseEntity<AuthenticationResponse.RefreshResponse> refresh(String refreshToken) {
        AuthenticationToken authenticationToken = tokenProvider.refresh(refreshToken);
        return authenticationProcessor.getRefreshResponse(authenticationToken);
    }

    public ResponseEntity<Void> authorizationCode(String providerType, String state) {
        return authenticationProcessor.getAuthorizationCode(providerType, state);
    }

    public AuthenticationToken socialLogin(String providerType, String authorizationCode, String state) {
        Member member = authenticationProcessor.socialLogin(providerType, authorizationCode, state);
        return tokenProvider.create(member.getId());
    }
}
