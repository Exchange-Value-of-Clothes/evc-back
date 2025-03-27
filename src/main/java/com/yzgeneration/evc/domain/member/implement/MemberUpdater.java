package com.yzgeneration.evc.domain.member.implement;

import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUpdater {

    private final MemberRepository memberRepository;

    public void active(Long memberId) {
        Member member = memberRepository.getById(memberId);
        member.active();
        memberRepository.save(member);
    }

    public Member changeNickname(Long memberId, String nickname) {
        Member member = memberRepository.getById(memberId);
        member.getMemberPrivateInformation().changeNickname(nickname);
        memberRepository.save(member);
        return member;
    }
}
