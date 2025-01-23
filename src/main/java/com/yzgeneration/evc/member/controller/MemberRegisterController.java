package com.yzgeneration.evc.member.controller;

import com.yzgeneration.evc.member.dto.MemberRequest.EmailSignup;
import com.yzgeneration.evc.member.dto.MemberResponse.RegisterResponse;
import com.yzgeneration.evc.member.model.Member;
import com.yzgeneration.evc.member.service.MemberRegisterService;
import com.yzgeneration.evc.verification.model.EmailVerification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members/register")
@RequiredArgsConstructor
public class MemberRegisterController {

    private final MemberRegisterService memberRegisterService;

    @PostMapping
    public RegisterResponse register(@RequestBody @Valid EmailSignup emailSignup) {
        Member member = memberRegisterService.createMemberByEmail(emailSignup);
        EmailVerification emailVerification = memberRegisterService.sendEmailForRequestVerification(member);
        return new RegisterResponse(emailVerification.getVerificationCode());
    }

    @GetMapping("/verify")
    public String verify(@RequestParam("code") String code) {
        memberRegisterService.verify(code);
        return "success";
    }

    @GetMapping("/request-code")
    public String resendVerificationCode(@RequestParam("email") String email) {
        return memberRegisterService.resendVerificationCode(email);
    }

}
