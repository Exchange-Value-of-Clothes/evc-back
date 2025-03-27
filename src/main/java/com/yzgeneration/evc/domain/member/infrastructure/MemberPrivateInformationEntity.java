package com.yzgeneration.evc.domain.member.infrastructure;

import com.yzgeneration.evc.domain.member.model.MemberPrivateInformation;
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

    public static MemberPrivateInformationEntity from(MemberPrivateInformation memberPrivateInformation) {
        return MemberPrivateInformationEntity.builder()
                .nickname(memberPrivateInformation.getNickname())
                .email(memberPrivateInformation.getEmail())
                .accountNumber(memberPrivateInformation.getAccountNumber())
                .accountName(memberPrivateInformation.getAccountName())
                .phoneNumber(memberPrivateInformation.getPhoneNumber())
                .build();
    }

    public MemberPrivateInformation toModel() {
        return MemberPrivateInformation.builder()
                .nickname(nickname)
                .email(email)
                .accountNumber(accountNumber)
                .accountName(accountName)
                .phoneNumber(phoneNumber)
                .build();
    }
}
