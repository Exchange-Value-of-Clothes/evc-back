package com.yzgeneration.evc.member.model;

import com.yzgeneration.evc.common.service.port.RandomHolder;
import lombok.Builder;
import lombok.Getter;

import static com.yzgeneration.evc.member.dto.MemberRequest.*;

@Getter
@Builder
public class MemberPrivateInformation {

    private String nickname;
    private String email;
    private String accountNumber;
    private String accountName;
    private String phoneNumber;
    private int point;

    public static MemberPrivateInformation createByEmail(EmailSignup emailSignup, RandomHolder randomHolder) {
        return MemberPrivateInformation.builder()
                .nickname(emailSignup.getNickname() + "#" + randomHolder.randomFourDigit())
                .email(emailSignup.getEmail())
                .build();
    }

}
