package com.yzgeneration.evc.member.implement;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.yzgeneration.evc.common.exception.ErrorCode.*;

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
