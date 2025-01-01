package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.member.dto.MemberRequest.MemberEmailCreate;
import com.yzgeneration.evc.member.enums.MemberRole;
import com.yzgeneration.evc.member.enums.MemberStatus;
import com.yzgeneration.evc.member.service.port.MemberRepository;
import com.yzgeneration.evc.member.model.Member;
import com.yzgeneration.evc.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.member.service.port.PasswordProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberRegisterService {

    private final MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor;

    @Transactional
    public Member createByEmail(MemberEmailCreate memberEmailCreate) {
        Member member = createMember(memberEmailCreate);
        return memberRepository.save(member);
    }

    private Member createMember(MemberEmailCreate memberEmailCreate) {
        MemberPrivateInformation privateInfo = MemberPrivateInformation.createByEmail(memberEmailCreate);
        MemberAuthenticationInformation authenticationInfo = MemberAuthenticationInformation.createByEmail(passwordProcessor, memberEmailCreate.getPassword());
        return Member.create(privateInfo, authenticationInfo, MemberRole.USER, MemberStatus.PENDING);
    }



}