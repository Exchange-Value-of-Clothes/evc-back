package com.yzgeneration.evc.member.implement;

import com.yzgeneration.evc.member.enums.ProviderType;
import com.yzgeneration.evc.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validate(MemberPrivateInformation privateInfo, MemberAuthenticationInformation authenticationInfo) {
        if (memberRepository.checkDuplicateEmail(privateInfo.getEmail())) {
            throw new RuntimeException();
        }
        if (authenticationInfo.getProviderType() != ProviderType.EMAIL) {
            throw new RuntimeException();
        }
    }
}
