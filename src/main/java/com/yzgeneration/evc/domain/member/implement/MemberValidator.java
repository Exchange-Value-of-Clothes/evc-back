package com.yzgeneration.evc.domain.member.implement;

import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.domain.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.domain.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.domain.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.yzgeneration.evc.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validate(MemberPrivateInformation privateInfo, MemberAuthenticationInformation authenticationInfo) {
        if (memberRepository.checkDuplicateEmail(privateInfo.getEmail())) {
            throw new CustomException(EMAIL_DUPLICATION);
        }
    }
}
