package com.yzgeneration.evc.domain.member.authentication.controller;

import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.authentication.service.AuthenticationService;
import com.yzgeneration.evc.external.social.CsrfRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public RefreshResponse refresh(@RequestParam("refreshToken") String refreshToken) {
        AuthenticationToken authenticationToken = authenticationService.refresh(refreshToken);
        return new RefreshResponse(authenticationToken);
    }

    @GetMapping("/code")
    public void authorizationCode(@RequestParam("state") String state) {

    }

}
