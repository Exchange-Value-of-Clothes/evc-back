package com.yzgeneration.evc.domain.member.model;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;
import com.yzgeneration.evc.domain.member.enums.MemberRole;
import com.yzgeneration.evc.domain.member.enums.MemberStatus;
import com.yzgeneration.evc.domain.member.service.port.PasswordProcessor;
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

    public void checkPassword(String rawPassword, PasswordProcessor passwordProcessor) {
        if (passwordProcessor.matches(rawPassword, memberAuthenticationInformation.getPassword())) {
            return;
        }
        throw new CustomException(ErrorCode.LOGIN_FAILED);
    }

    public void active() {
        this.memberStatus = MemberStatus.ACTIVE;
    }

    public void checkStatus() {
        memberStatus.checkStatus();
    }

}
