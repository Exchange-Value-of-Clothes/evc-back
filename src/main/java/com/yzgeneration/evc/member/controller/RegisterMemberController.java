package com.yzgeneration.evc.member.controller;

import com.yzgeneration.evc.member.dto.MemberRequest.EmailSignup;
import com.yzgeneration.evc.member.dto.MemberResponse.RegisterResponse;
import com.yzgeneration.evc.member.model.Member;
import com.yzgeneration.evc.member.service.MemberAuthenticationService;
import com.yzgeneration.evc.verification.model.EmailVerification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/register")
@RequiredArgsConstructor
public class RegisterMemberController {

    private final MemberAuthenticationService memberAuthenticationService;

    @PostMapping
    public RegisterResponse register(@RequestBody @Valid EmailSignup emailSignup) {
        Member member = memberAuthenticationService.createByEmail(emailSignup);
        EmailVerification emailVerification = memberAuthenticationService.sendEmailForVerification(member);
        return new RegisterResponse(emailVerification.getVerificationCode());
    }

}
