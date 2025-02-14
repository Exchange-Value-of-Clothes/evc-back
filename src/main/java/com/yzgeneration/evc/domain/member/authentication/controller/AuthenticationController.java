package com.yzgeneration.evc.domain.member.authentication.controller;

import com.yzgeneration.evc.validator.EnumValidator;
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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@CookieValue(name = "refresh_token") String refreshToken) {
        return authenticationService.refresh(refreshToken);
    }

    @GetMapping("/social")
    public ResponseEntity<Void> social_login(@RequestParam("provider_type") String providerType, @RequestParam("state") String state) {
        EnumValidator.validate(ProviderType.class, "providerType", providerType);
        return authenticationService.authorizationCode(providerType, state);
    }

    @GetMapping("/access-token")
    public ResponseEntity<LoginResponse> getUserInformation(@RequestParam("provider_type") String providerType, @RequestParam("code") String authorizationCode, @RequestParam("state") String state) {
        return authenticationService.socialLogin(providerType, authorizationCode, state);
    }

}
