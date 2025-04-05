package com.yzgeneration.evc.domain.member.service;

import com.yzgeneration.evc.domain.member.infrastructure.MemberRepository;
import com.yzgeneration.evc.domain.member.infrastructure.PasswordProcessor;
import com.yzgeneration.evc.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAccountService {

    private final MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor;

    public void changeEmail(String email, Long memberId) {
        Member member = memberRepository.getById(memberId);
        member.getMemberPrivateInformation().changeEmail(email);
        memberRepository.save(member);
    }

    public void changePassword(String oldPassword, String newPassword, Long memberId) {
        Member member = memberRepository.getById(memberId);
        member.getMemberAuthenticationInformation().changePassword(oldPassword, newPassword, passwordProcessor);
        memberRepository.save(member);
    }
}
