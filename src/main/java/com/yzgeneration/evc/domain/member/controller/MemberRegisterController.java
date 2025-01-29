package com.yzgeneration.evc.domain.member.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.member.dto.MemberRequest.EmailSignup;
import com.yzgeneration.evc.domain.member.dto.MemberResponse.RegisterResponse;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.service.MemberRegisterService;
import com.yzgeneration.evc.domain.verification.model.EmailVerification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.yzgeneration.evc.domain.member.dto.MemberResponse.*;

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
    public CommonResponse verify(@RequestParam("code") String code) {
        memberRegisterService.verify(code);
        return CommonResponse.success();
    }

    @GetMapping("/request-code")
    public ResendCodeResponse resendVerificationCode(@RequestParam("email") String email) {
        return new ResendCodeResponse(memberRegisterService.resendVerificationCode(email));
    }

}
