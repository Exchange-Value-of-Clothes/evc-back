package com.yzgeneration.evc.authentication.implement;

import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import com.yzgeneration.evc.domain.member.service.port.PasswordProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {

    private final MemberRepository memberRepository;
    private final PasswordProcessor passwordProcessor;

    public Member login(String email, String password) {
        Member member = memberRepository.getByEmail(email);
        member.checkPassword(password, passwordProcessor);
        member.checkStatus();
        return member;
    }

}
