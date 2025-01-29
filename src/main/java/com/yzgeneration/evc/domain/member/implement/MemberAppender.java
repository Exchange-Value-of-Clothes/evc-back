package com.yzgeneration.evc.domain.member.implement;

import com.yzgeneration.evc.common.service.port.RandomHolder;
import com.yzgeneration.evc.domain.member.enums.MemberRole;
import com.yzgeneration.evc.domain.member.enums.MemberStatus;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.domain.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import com.yzgeneration.evc.domain.member.service.port.PasswordProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.yzgeneration.evc.domain.member.dto.MemberRequest.*;

@Component
@RequiredArgsConstructor
public class MemberAppender {

    private final MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor;
    private final RandomHolder randomHolder;
    private final MemberValidator memberValidator;

    public Member createByEmail(EmailSignup emailSignup) {
        MemberPrivateInformation privateInfo = MemberPrivateInformation.createdByEmail(emailSignup, randomHolder);
        MemberAuthenticationInformation authenticationInfo = MemberAuthenticationInformation.createdByEmail(emailSignup.getPassword(), passwordProcessor);
        memberValidator.validate(privateInfo, authenticationInfo);
        Member member = Member.create(privateInfo, authenticationInfo, MemberRole.USER, MemberStatus.PENDING);
        return memberRepository.save(member);
    }
}
