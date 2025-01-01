package com.yzgeneration.evc.member.model;

import com.yzgeneration.evc.member.enums.MemberRole;
import com.yzgeneration.evc.member.enums.MemberStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Member {

    private Long id;
    private MemberPrivateInformation memberPrivateInformation;
    private MemberAuthenticationInformation memberAuthenticationInformation;
    private MemberRole memberRole;
    private MemberStatus memberStatus;

    public static Member create(MemberPrivateInformation memberPrivateInformation,
                                MemberAuthenticationInformation memberAuthenticationInformation,
                                MemberRole memberRole, MemberStatus memberStatus) {
        return Member.builder()
                .memberPrivateInformation(memberPrivateInformation)
                .memberAuthenticationInformation(memberAuthenticationInformation)
                .memberRole(memberRole)
                .memberStatus(memberStatus)
                .build();
    }
}
