package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.member.dto.MemberRequest.EmailSignup;

import com.yzgeneration.evc.member.implement.MemberCreator;

import com.yzgeneration.evc.member.model.Member;

import com.yzgeneration.evc.verification.enums.EmailVerificationType;
import com.yzgeneration.evc.verification.model.EmailVerification;
import com.yzgeneration.evc.verification.implement.EmailVerificationProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberRegisterService {

    private final MemberCreator memberCreator;
    private final EmailVerificationProcessor emailVerificationProcessor;

    public Member createMemberByEmail(EmailSignup emailSignup) {
        return memberCreator.createByEmail(emailSignup);
    }

    public EmailVerification sendEmailForRequestVerification(Member member) {
        EmailVerification emailVerification = emailVerificationProcessor.createEmailVerification(member.getId(), member.getMemberPrivateInformation().getEmail(), EmailVerificationType.REGISTER);
        emailVerificationProcessor.sendMail(emailVerification);
        return emailVerification;
    }

    public void verify(String code) {
        emailVerificationProcessor.verify(code);
    }

    public String resendVerificationCode(String email) {
        return emailVerificationProcessor.get(email);
    }

}