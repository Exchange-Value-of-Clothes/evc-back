package com.yzgeneration.evc.authentication.controller;

import com.yzgeneration.evc.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yzgeneration.evc.authentication.dto.AuthenticationRequest.*;
import static com.yzgeneration.evc.authentication.dto.AuthenticationResponse.*;

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


}
