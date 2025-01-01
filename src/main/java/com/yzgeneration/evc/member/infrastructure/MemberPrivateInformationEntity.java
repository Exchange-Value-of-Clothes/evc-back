package com.yzgeneration.evc.member.infrastructure;

import com.yzgeneration.evc.member.model.MemberPrivateInformation;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MemberPrivateInformationEntity {

    private String nickname;
    private String email;
    private String accountNumber;
    private String accountName;
    private String phoneNumber;
    private int point;

    public static MemberPrivateInformationEntity from(MemberPrivateInformation memberPrivateInformation) {
        return MemberPrivateInformationEntity.builder()
                .nickname(memberPrivateInformation.getNickname())
                .email(memberPrivateInformation.getEmail())
                .accountNumber(memberPrivateInformation.getAccountNumber())
                .accountName(memberPrivateInformation.getAccountName())
                .phoneNumber(memberPrivateInformation.getPhoneNumber())
                .point(memberPrivateInformation.getPoint())
                .build();
    }

    public MemberPrivateInformation toModel() {
        return MemberPrivateInformation.builder()
                .nickname(nickname)
                .email(email)
                .accountNumber(accountNumber)
                .accountName(accountName)
                .phoneNumber(phoneNumber)
                .point(point)
                .build();
    }
}