package com.yzgeneration.evc.domain.member.model;

import com.yzgeneration.evc.common.service.port.RandomHolder;
import lombok.Builder;
import lombok.Getter;

import static com.yzgeneration.evc.domain.member.dto.MemberRequest.*;

@Getter
@Builder
public class MemberPrivateInformation {

    private String nickname;
    private String email;
    private String accountNumber;
    private String accountName;
    private String phoneNumber;
    private int point;

    public static MemberPrivateInformation createdByEmail(EmailSignup emailSignup, RandomHolder randomHolder) {
        return MemberPrivateInformation.builder()
                .nickname(emailSignup.getNickname() + "#" + randomHolder.randomFourDigit())
                .email(emailSignup.getEmail())
                .build();
    }

}
