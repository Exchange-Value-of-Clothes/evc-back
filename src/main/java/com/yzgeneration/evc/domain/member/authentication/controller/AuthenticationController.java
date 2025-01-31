package com.yzgeneration.evc.domain.member.authentication.controller;

import com.yzgeneration.evc.validator.EnumValidator;
import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.authentication.service.AuthenticationService;
import com.yzgeneration.evc.domain.member.enums.ProviderType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationRequest.*;
import static com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationResponse.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthenticationToken authenticationToken = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return new LoginResponse(authenticationToken);
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@RequestParam("refresh-token") String refreshToken) {
        AuthenticationToken authenticationToken = authenticationService.refresh(refreshToken);
        return new RefreshResponse(authenticationToken);
    }

    @GetMapping("/social")
    public ResponseEntity<Void> social_login(@RequestParam("provider_type") String providerType, @RequestParam("state") String state) {
        EnumValidator.validate(ProviderType.class, "providerType", providerType);
        return authenticationService.authorizationCode(providerType, state);
    }

    @GetMapping("/access-token")
    public SocialLoginResponse getUserInformation(@RequestParam("provider_type") String providerType, @RequestParam("code") String authorizationCode, @RequestParam("state") String state) {
        AuthenticationToken authenticationToken = authenticationService.socialLogin(providerType, authorizationCode, state);
        return new SocialLoginResponse(authenticationToken);
    }




}
