package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.common.service.port.RandomHolder;
import com.yzgeneration.evc.member.dto.MemberRequest.EmailSignup;
import com.yzgeneration.evc.member.enums.MemberRole;
import com.yzgeneration.evc.member.enums.MemberStatus;
import com.yzgeneration.evc.member.implement.MemberCreator;
import com.yzgeneration.evc.member.service.port.MemberRepository;
import com.yzgeneration.evc.member.model.Member;
import com.yzgeneration.evc.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.member.service.port.PasswordProcessor;
import com.yzgeneration.evc.verification.enums.EmailVerificationType;
import com.yzgeneration.evc.verification.model.EmailVerification;
import com.yzgeneration.evc.verification.implement.EmailVerificationProcessor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthenticationService {

    private final MemberCreator memberCreator;
    private final EmailVerififcationProcessor emailVerificationProcessor;

    public Member createMemberByEmail(EmailSignup emailSignup) {
        return memberCreator.createByEmail(emailSignup);
    }

    public EmailVerification sendEmailForVerification(Member member) {
        EmailVerification emailVerification = emailVerificationProcessor.createEmailVerification(member.getId(), member.getMemberPrivateInformation().getEmail(), EmailVerificationType.REGISTER);
        emailVerificationProcessor.sendMail(emailVerification);
        return emailVerification;
    }




}