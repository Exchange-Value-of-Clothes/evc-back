package com.yzgeneration.evc.member.model;

import com.yzgeneration.evc.member.dto.MemberRequest;
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

    public static MemberPrivateInformation createByEmail(MemberEmailCreate memberEmailCreate) {
        return MemberPrivateInformation.builder()
                .nickname(memberEmailCreate.getNickname())
                .email(memberEmailCreate.getEmail())
                .build();
    }

}
