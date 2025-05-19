package com.yzgeneration.evc.domain.member.service;

import com.yzgeneration.evc.domain.member.MemberCreatedEvent;
import com.yzgeneration.evc.domain.member.dto.MemberRequest.EmailSignup;

import com.yzgeneration.evc.domain.member.implement.MemberAppender;

import com.yzgeneration.evc.domain.member.implement.MemberUpdater;
import com.yzgeneration.evc.domain.member.model.Member;

import com.yzgeneration.evc.domain.verification.enums.EmailVerificationType;
import com.yzgeneration.evc.domain.verification.model.EmailVerification;
import com.yzgeneration.evc.domain.verification.implement.EmailVerificationProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberRegisterService {

    private final MemberAppender memberAppender;
    private final MemberUpdater memberUpdater;
    private final EmailVerificationProcessor emailVerificationProcessor;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Member createMemberByEmail(EmailSignup emailSignup) {
        Member member = memberAppender.createByEmail(emailSignup);
        eventPublisher.publishEvent(new MemberCreatedEvent(member.getId(), null));
        return member;
    }

    public EmailVerification sendEmailForRequestVerification(Member member) {
        EmailVerification emailVerification = emailVerificationProcessor.createEmailVerification(member.getId(), member.getMemberPrivateInformation().getEmail(), EmailVerificationType.REGISTER);
        emailVerificationProcessor.sendMail(emailVerification);
        return emailVerification;
    }

    public void verify(String code) {
        Long memberId = emailVerificationProcessor.verify(code);
        memberUpdater.active(memberId);
    }

    public String resendVerificationCode(String email) {
        return emailVerificationProcessor.get(email);
    }

}