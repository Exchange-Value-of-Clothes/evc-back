package com.yzgeneration.evc.domain.member.implement;

import com.yzgeneration.evc.common.implement.port.RandomHolder;
import com.yzgeneration.evc.domain.member.enums.MemberRole;
import com.yzgeneration.evc.domain.member.enums.MemberStatus;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.domain.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.domain.member.infrastructure.MemberRepository;
import com.yzgeneration.evc.domain.member.infrastructure.PasswordProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static com.yzgeneration.evc.domain.member.dto.MemberRequest.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAppender {

    private final MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor;
    private final RandomHolder randomHolder;
    private final MemberValidator memberValidator;

    public Member createByEmail(EmailSignup emailSignup) {
        log.info("createByEmail - Transaction active : {}", TransactionSynchronizationManager.isActualTransactionActive());
        MemberPrivateInformation privateInfo = MemberPrivateInformation.createdByEmail(emailSignup.getNickname(), emailSignup.getEmail(), randomHolder);
        MemberAuthenticationInformation authenticationInfo = MemberAuthenticationInformation.createdByEmail(emailSignup.getPassword(), passwordProcessor);
        memberValidator.validate(privateInfo, authenticationInfo);
        Member member = Member.create(privateInfo, authenticationInfo, MemberRole.USER, MemberStatus.PENDING);
        return memberRepository.save(member);
    }
}
