package com.yzgeneration.evc.domain.member.implement;

import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.domain.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.domain.member.model.MemberPrivateInformation;
import com.yzgeneration.evc.domain.member.infrastructure.MemberRepository;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.yzgeneration.evc.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private static final String NICKNAME_REGEX = "^[a-zA-Z0-9가-힣 ]{2,16}";
    private static final Pattern pattern = Pattern.compile(NICKNAME_REGEX);

    private final MemberRepository memberRepository;

    public void validate(MemberPrivateInformation privateInfo, MemberAuthenticationInformation authenticationInfo) {
        if (memberRepository.checkDuplicateEmail(privateInfo.getEmail())) {
            throw new CustomException(EMAIL_DUPLICATION);
        }
    }

    public static void nickname(String nickname) {
        if(!pattern.matcher(nickname).matches()) throw new CustomException(ErrorCode.INVALID_NICKNAME);
    }
}
