package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.common.service.port.RandomHolder;
import com.yzgeneration.evc.member.dto.MemberRequest.EmailSignup;
import com.yzgeneration.evc.member.enums.MemberRole;
import com.yzgeneration.evc.member.enums.MemberStatus;
import com.yzgeneration.evc.member.service.port.MemberRepository;
import com.yzgeneration.evc.member.model.Member;
import com.yzgeneration.evc.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.member.service.port.PasswordProcessor;
import com.yzgeneration.evc.verification.enums.EmailVerificationType;
import com.yzgeneration.evc.verification.model.EmailVerification;
import com.yzgeneration.evc.verification.implement.EmailVerificationProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAuthenticationService {

    private final MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor;
    private final EmailVerificationProcessor emailVerificationProcessor;
    private final RandomHolder randomHolder;

    @Transactional
    public Member createByEmail(EmailSignup emailSignup) {
        Member member = createMember(emailSignup);
        return memberRepository.save(member);
    }

    public EmailVerification sendEmailForVerification(Member member) {
        EmailVerification emailVerification = emailVerificationProcessor.createEmailVerification(member.getId(), member.getMemberPrivateInformation().getEmail(), EmailVerificationType.REGISTER);
        emailVerificationProcessor.sendMail(emailVerification);
        return emailVerification;
    }

    private Member createMember(EmailSignup emailSignup) {
        MemberPrivateInformation privateInfo = MemberPrivateInformation.createByEmail(emailSignup, randomHolder);
        MemberAuthenticationInformation authenticationInfo = MemberAuthenticationInformation.createByEmail(passwordProcessor, emailSignup.getPassword());
        return Member.create(privateInfo, authenticationInfo, MemberRole.USER, MemberStatus.PENDING);
    }



}